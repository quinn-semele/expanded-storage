import dev.compasses.multiloader.Constants
import gradle.kotlin.dsl.accessors._cbfc74c95d5f89f5a9d8d1cb7ae6b5ef.main
import gradle.kotlin.dsl.accessors._cbfc74c95d5f89f5a9d8d1cb7ae6b5ef.sourceSets
import org.gradle.kotlin.dsl.invoke

plugins {
    id("multiloader-loader")
    id("fabric-loom")
}

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
    }
}

configurations {
    create("threadJava") { isCanBeResolved = false; isCanBeConsumed = true }
    create("threadResources") { isCanBeResolved = false; isCanBeConsumed = true }
}

artifacts {
    add("threadJava", sourceSets.main.map { it.java.sourceDirectories.singleFile })
    add("threadResources", sourceSets.main.map { it.resources.sourceDirectories.first() })
    add("threadResources", sourceSets.main.map { it.resources.sourceDirectories.last() })
}
