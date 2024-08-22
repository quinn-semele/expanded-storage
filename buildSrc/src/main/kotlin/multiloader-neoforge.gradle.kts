import dev.compasses.multiloader.toTitleCase
import dev.compasses.multiloader.Constants
import dev.compasses.multiloader.task.ProcessJsonTask

plugins {
    id("multiloader-loader")
    id("net.neoforged.moddev")
}

evaluationDependsOn(":common")

neoForge {
    version = Constants.NEOFORGE_VERSION

    accessTransformers.from(project(":common").neoForge.accessTransformers.files)

    parchment {
        minecraftVersion = Constants.PARCHMENT_MINECRAFT
        mappingsVersion = Constants.PARCHMENT_RELEASE
    }

    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", Constants.MOD_ID)
            ideName = "NeoForge ${name.toTitleCase()} (${project.path})"
        }

        create("client") { client() }
        create("data") {
            data()

            programArguments.addAll(
                "--mod", Constants.MOD_ID,
                "--output", file("src/generated/resources").absolutePath,
                "--existing", findProject(":common")!!.file("src/main/resources").absolutePath,
                "--all"
            )
        }
        create("server") { server() }
    }

    mods {
        create(Constants.MOD_ID) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDirs("src/generated/resources")
}

tasks.jar {
    archiveClassifier = "fat"
}

tasks.processResources {
    exclude("*.accesswidener")
}

tasks.register("processJson", ProcessJsonTask::class) {
    group = "multiloader"
    dependsOn(tasks.jar)
    input.set(tasks.jar.get().outputs.files.singleFile)
    archiveClassifier = ""
}

tasks.build {
    dependsOn("processJson")
}