import me.modmuss50.mpp.ReleaseType
import org.codehaus.groovy.runtime.ProcessGroovyMethods
import semele.quinn.expandedstorage.plugin.Constants
import semele.quinn.expandedstorage.plugin.Versions
import semele.quinn.expandedstorage.plugin.task.AbstractJsonTask
import semele.quinn.expandedstorage.plugin.task.AbstractRestrictedTask
import semele.quinn.expandedstorage.plugin.task.BuildModTask
import semele.quinn.expandedstorage.plugin.task.ReleaseModTask

plugins {
    id("me.modmuss50.mod-publish-plugin") version "0.5.1"
}

tasks.create(Constants.BUILD_MOD_TASK, BuildModTask::class.java)
val releaseTask = tasks.register(Constants.RELEASE_MOD_TASK, ReleaseModTask::class.java) {
    dependsOn(":publishMods")
}

gradle.taskGraph.whenReady {
    for (task in allTasks) {
        (task as? AbstractRestrictedTask)?.doChecks()
    }
}

evaluationDependsOnChildren()

val releaseType = if ("alpha" in Versions.EXPANDEDSTORAGE) {
    ReleaseType.ALPHA
} else if ("beta" in Versions.EXPANDEDSTORAGE) {
    ReleaseType.BETA
} else {
    ReleaseType.STABLE
}

val modChangelog = rootProject.file("changelog.md").readLines(Charsets.UTF_8) +
        listOf("", "Commit: https://github.com/quinn-semele/expanded-storage/commit/${getGitCommit()}")

val commonCurseForgeOptions = publishMods.curseforgeOptions {
    accessToken = providers.environmentVariable("QUINN_CF_TOKEN")
    projectId = "978068"
    projectSlug = "expanded-storage"

    clientRequired = true
    serverRequired = true

    minecraftVersions.addAll(Versions.SUPPORTED_GAME_VERSIONS)
    javaVersions.add(JavaVersion.VERSION_17)
}

val commonModrinthOptions = publishMods.modrinthOptions {
    accessToken = providers.environmentVariable("QUINN_MR_TOKEN")
    projectId = "jCCPlP3c"

    minecraftVersions.addAll(Versions.SUPPORTED_GAME_VERSIONS)
}

val fabricOptions = publishMods.publishOptions {
    modLoaders.add("fabric")
    displayName = "ES Fabric ${Versions.EXPANDEDSTORAGE}"
    version = "${Versions.EXPANDEDSTORAGE}+fabric"
    file = project(":fabric").tasks.named<AbstractJsonTask>("minJar").map { it.archiveFile.get() }
}

val forgeOptions = publishMods.publishOptions {
    modLoaders.add("forge")
    displayName = "ES Forge ${Versions.EXPANDEDSTORAGE}"
    version = "${Versions.EXPANDEDSTORAGE}+forge"
    file = project(":forge").tasks.named<AbstractJsonTask>("minJar").map { it.archiveFile.get() }
}

val quiltOptions = publishMods.publishOptions {
    modLoaders.add("quilt")
    displayName = "ES Quilt ${Versions.EXPANDEDSTORAGE}"
    version = "${Versions.EXPANDEDSTORAGE}+quilt"
    file = project(":quilt").tasks.named<AbstractJsonTask>("minJar").map { it.archiveFile.get() }
}

val threadCurseForgeOptions = publishMods.curseforgeOptions {
    optional("htm")
    optional("carrier")
    optional("towelette")
    optional("roughly-enough-items")
    optional("modmenu")
    optional("amecs")
    optional("inventory-profiles-next")
    optional("emi")
    optional("inventory-tabs-updated")
    optional("jei")

}

val threadModrinthOptions = publishMods.modrinthOptions {
    optional("htm")
    optional("carrier")
    optional("towelette")
    optional("rei")
    optional("modmenu")
    optional("amecs")
    optional("inventory-profiles-next")
    optional("emi")
    optional("inventory-tabs-updated")
    optional("jei")
}

publishMods {
    changelog = modChangelog.joinToString("\n")
    type = releaseType

    dryRun = providers.systemProperty("MOD_UPLOAD_DEBUG").orElse("false").map { it == "true" }

    curseforge("CurseForgeFabric") {
        from(commonCurseForgeOptions, fabricOptions)

        requires("fabric-api")
        from(threadCurseForgeOptions)
    }

    curseforge("CurseForgeQuilt") {
        from(commonCurseForgeOptions, quiltOptions)

        requires("qsl")
        from(threadCurseForgeOptions)
    }

    curseforge("CurseForgeForge") {
        from(commonCurseForgeOptions, forgeOptions)

        optional("jei")
        optional("quark")
        optional("inventory-profiles-next")
        optional("roughly-enough-items")
    }

    modrinth("ModrinthFabric") {
        from(commonModrinthOptions, fabricOptions)

        requires("fabric-api")
        from(threadModrinthOptions)
    }

    modrinth("ModrinthQuilt") {
        from(commonModrinthOptions, quiltOptions)

        requires("qsl")
        from(threadModrinthOptions)
    }

    modrinth("ModrinthForge") {
        from(commonModrinthOptions, forgeOptions)

        optional("jei")
        optional("quark")
        optional("inventory-profiles-next")
        optional("rei")
    }
}

tasks.getByName("publishModrinthFabric").mustRunAfter("publishModrinthForge", "publishModrinthQuilt")
tasks.getByName("publishModrinthForge").mustRunAfter("publishModrinthQuilt")

tasks.getByName("publishCurseForgeFabric").mustRunAfter("publishCurseForgeForge", "publishCurseForgeQuilt")
tasks.getByName("publishCurseForgeForge").mustRunAfter("publishCurseForgeQuilt")

private fun getGitCommit(): String {
    return ProcessGroovyMethods.getText(ProcessGroovyMethods.execute("git rev-parse HEAD"))
}