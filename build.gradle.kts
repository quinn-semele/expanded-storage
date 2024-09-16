import dev.compasses.multiloader.Constants
import dev.compasses.multiloader.extension.DependencyType
import dev.compasses.multiloader.extension.MultiLoaderExtension
import dev.compasses.multiloader.extension.UploadTarget
import dev.compasses.multiloader.task.ProcessJsonTask
import me.modmuss50.mpp.Platform
import me.modmuss50.mpp.PublishModTask
import me.modmuss50.mpp.PublishResult
import me.modmuss50.mpp.ReleaseType
import org.codehaus.groovy.runtime.ProcessGroovyMethods
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.security.MessageDigest

plugins {
    id("me.modmuss50.mod-publish-plugin") version "0.6.3"
}

// To update run the :wrapper task, update values here as required from the gradle site below.
// https://gradle.org/release-checksums/
tasks.wrapper {
    gradleVersion = "8.10"
    distributionSha256Sum = "5b9c5eb3f9fc2c94abaea57d90bd78747ca117ddbbf96c859d3741181a12bf2a"
    distributionType = Wrapper.DistributionType.BIN
}

gradle.taskGraph.whenReady {
    if (hasTask(":publishMods") && !providers.environmentVariable("CI").isPresent) {
        throw IllegalStateException("Cannot publish mods locally, please run the release workflow on GitHub.")
    }
}

// This feels like a hack, but I can't really think of a way to do this properly.
evaluationDependsOnChildren()

val requestedProjects = providers.environmentVariable("MULTILOADER_PUBLISH_PROJECTS").getOrElse("neoforge,fabric,quilt").split(",")

val projectsToPublish = mapOf(
    "NeoForge" to findProject(":neoforge"),
    "Fabric" to findProject(":fabric"),
    "Quilt" to findProject(":quilt")
).filter { it.value != null }
 .mapValues { (_, loader) -> loader!! }
 .filter { it.value.name in requestedProjects }

val modChangelog = providers.provider {
    val compareTag = ProcessGroovyMethods.getText(ProcessGroovyMethods.execute("git describe --tags --abbrev=0")).trim()
    val commitHash = ProcessGroovyMethods.getText(ProcessGroovyMethods.execute("git rev-parse HEAD")).trim()

    buildString {
        appendLine(file("changelog.md").readText(Charsets.UTF_8).trimEnd())

        if (compareTag.isNotBlank()) {
            appendLine()
            appendLine("A detailed changelog can be found [here](${Constants.COMPARE_URL}${compareTag}...${commitHash}).")
        }
    }
}

val curseforgeOptions = Constants.curseforgeProperties?.let { props ->
    publishMods.curseforgeOptions {
        accessToken = providers.environmentVariable(props.uploadToken)
        projectId = props.projectId
        projectSlug = props.projectSlug
        minecraftVersions = Constants.SUPPORTED_MINECRAFT_VERSIONS
        clientRequired = props.clientSideRequired
        serverRequired = props.serverSideRequired
        javaVersions = props.supportedJavaVersions
    }
}

val modrinthOptions = Constants.modrinthProperties?.let { props ->
    publishMods.modrinthOptions {
        accessToken = providers.environmentVariable(props.uploadToken)
        projectId = props.projectId
        minecraftVersions = Constants.SUPPORTED_MINECRAFT_VERSIONS
    }
}

publishMods {
    changelog = modChangelog
    type = if ("alpha" in Constants.MOD_VERSION) {
        ReleaseType.ALPHA
    } else if ("beta" in Constants.MOD_VERSION) {
        ReleaseType.BETA
    } else {
        ReleaseType.STABLE
    }

    dryRun = providers.environmentVariable("MULTILOADER_DRY_RUN").map { it == "true" }.orElse(false)
}

val publishTasks = projectsToPublish.map { (name, loader) ->
    name to buildList {
        Constants.curseforgeProperties?.run {
            add(publishMods.curseforge("CurseForge$name") {
                from(curseforgeOptions!!)
                displayName = "$name ${loader.version}"
                version = "${Constants.MOD_VERSION}+${name.lowercase()}"
                modLoaders.add(name.lowercase())

                file = loader.tasks.getByName("processJson", ProcessJsonTask::class).archiveFile

                dependencies {
                    val multiloaderExt = loader.extensions.getByName<MultiLoaderExtension>("multiloader")

                    optional(*multiloaderExt.getDependencyIds(UploadTarget.CURSEFORGE, DependencyType.OPTIONAL).toTypedArray())
                    requires(*multiloaderExt.getDependencyIds(UploadTarget.CURSEFORGE, DependencyType.REQUIRED).toTypedArray())
                }
            } as NamedDomainObjectProvider<Platform>)
        }

        Constants.modrinthProperties?.run {
            add(publishMods.modrinth("Modrinth$name") {
                from(modrinthOptions!!)
                displayName = "$name ${loader.version}"
                modLoaders.add(name.lowercase())

                file = loader.tasks.getByName("processJson", ProcessJsonTask::class).archiveFile
                version = provider {
                    val bytes = MessageDigest.getInstance("SHA-256").digest(file.get().asFile.readBytes())

                    bytes.fold("") { str, it -> str + "%02x".format(it) }.take(32)
                }

                dependencies {
                    val multiloaderExt = loader.extensions.getByName<MultiLoaderExtension>("multiloader")

                    optional(*multiloaderExt.getDependencyIds(UploadTarget.MODRINTH, DependencyType.OPTIONAL).toTypedArray())
                    requires(*multiloaderExt.getDependencyIds(UploadTarget.MODRINTH, DependencyType.REQUIRED).toTypedArray())
                }
            } as NamedDomainObjectProvider<Platform>)
        }
    }
}.toMap()

tasks.publishMods {
    doLast {
        val environmentVariable = providers.environmentVariable(Constants.PUBLISH_WEBHOOK_VARIABLE)

        if (environmentVariable.isPresent) {
            val uri = uri(environmentVariable.get())

            val results = publishTasks.mapValues { (_, publishTasks) -> publishTasks.map { task ->
                PublishResult.fromJson(tasks.getByName<PublishModTask>(task.get().taskName).result.get().asFile.readText(Charsets.UTF_8))
            } }

            val cfLinks = results.mapValues { it.value.firstOrNull { it.type == "curseforge" } }.filter { it.value != null }.mapValues { it.value!!.link }
            val mrLinks = results.mapValues { it.value.firstOrNull { it.type == "modrinth" } }.filter { it.value != null }.mapValues { it.value!!.link }

            val payload = buildString {
                append("""{"content": """")

                append("""
                    |**${Constants.MOD_NAME} ${Constants.MOD_VERSION}** for **${Constants.MINECRAFT_VERSION}**
                    |${modChangelog.get()}
                    |:curseforge: ${cfLinks.map { "[${it.key}](<${it.value}>)" }.joinToString(" | ")}
                    |:modrinth: ${mrLinks.map { "[${it.key}](<${it.value}>)" }.joinToString(" | ")}
                """.trimMargin().trim().replace("\n", "\\n"))

                append(""""}""")
            }

            val request = HttpRequest.newBuilder(uri).header("Content-Type", "application/json").POST(BodyPublishers.ofString(payload)).build()
            val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() !in 200..299) {
                project.logger.error("Failed to publish release notes to webhook:\n${response.body()}")
            }
        }
    }
}

tasks.register("logVersion") {
    doFirst {
        println("mod-version=${Constants.MOD_VERSION}+${Constants.MINECRAFT_VERSION}")
    }
}
