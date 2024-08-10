import dev.compasses.multiloader.Constants
import dev.compasses.multiloader.task.ProcessJsonTask

plugins {
    id("multiloader-loader")
    id("fabric-loom")
}

configurations {
    create("threadJava") { isCanBeResolved = true }
    create("threadResources") { isCanBeResolved = true }
}

dependencies {
    minecraft("com.mojang:minecraft:${Constants.MINECRAFT_VERSION}")

    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${Constants.PARCHMENT_MINECRAFT}:${Constants.PARCHMENT_RELEASE}@zip")
    })

    compileOnly(project(":thread"))

    "threadJava"(project(path=":thread", configuration="threadJava"))
    "threadResources"(project(path=":thread", configuration="threadResources"))
}

tasks {
    "compileJava"(JavaCompile::class) {
        dependsOn(configurations.getByName("threadJava"))
        source(configurations.getByName("threadJava"))
    }

    processResources {
        dependsOn(configurations.getByName("threadResources"))
        from(configurations.getByName("threadResources")) {
            exclude("fabric.mod.json")
        }
    }
}

loom {
    val accessWidener = project(":common").file("src/main/resources/${Constants.MOD_ID}.accesswidener")
    if (accessWidener.exists()) {
        accessWidenerPath = accessWidener
    }

    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName = "${Constants.MOD_ID}.refmap.json"
        useLegacyMixinAp = false
    }
}

tasks.remapJar.configure {
    archiveClassifier = "fat"
}

tasks.register("minJar", ProcessJsonTask::class) {
    group = "multiloader"
    dependsOn(tasks.remapJar)
    input.set(tasks.remapJar.get().outputs.files.singleFile)
    archiveClassifier = ""
}

tasks.build.configure {
    dependsOn("minJar")
}