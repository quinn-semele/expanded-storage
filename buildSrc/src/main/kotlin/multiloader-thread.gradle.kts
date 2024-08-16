import dev.compasses.multiloader.Constants

plugins {
    id("multiloader-loader")
    id("org.quiltmc.loom")
}

evaluationDependsOn(":common")

dependencies {
    minecraft("com.mojang:minecraft:${Constants.MINECRAFT_VERSION}")

    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${Constants.PARCHMENT_MINECRAFT}:${Constants.PARCHMENT_RELEASE}@zip")
    })

    modCompileOnly(modRuntimeOnly("net.fabricmc:fabric-loader:${Constants.FABRIC_LOADER_VERSION}")!!)
    modCompileOnly(modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${Constants.FABRIC_API_VERSION}")!!)
}

fabricApi {
    configureDataGeneration {
        modId = Constants.MOD_ID
        outputDirectory = file("src/generated/resources")
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

    runs {
        named("client") {
            client()

            configName = "Thread Client"
            isIdeConfigGenerated = true
        }

        named("server") {
            server()

            configName = "Thread Server"
            isIdeConfigGenerated = true
        }

        named("datagen") {
            configName = "Thread Data"
            isIdeConfigGenerated = true
        }
    }
}

configurations {
    create("threadJava") { isCanBeResolved = false; isCanBeConsumed = true }
    create("threadResources") { isCanBeResolved = false; isCanBeConsumed = true }
}

artifacts {
    val mainSourceSet = sourceSets.main.get()

    for (sourceDirectory in mainSourceSet.java.sourceDirectories) {
        add("threadJava", sourceDirectory)
    }

    for (sourceDirectory in mainSourceSet.resources.sourceDirectories) {
        add("threadResources", sourceDirectory)
    }
}

configurations.whenObjectAdded whenConfigurationAdded@ {
    if (name == "modRuntimeClasspathMainMapped") {
        dependencies.whenObjectAdded {
            if (name == "fabric-loader" && group == "net.fabricmc") {
                this@whenConfigurationAdded.exclude(group = group, module = name)
            } else if (name == "quilt-loader" && group == "org.quiltmc") {
                this@whenConfigurationAdded.exclude(group = group, module = name)
            }
        }
    }
}
