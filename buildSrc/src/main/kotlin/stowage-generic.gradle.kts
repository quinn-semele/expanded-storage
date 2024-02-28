import semele.quinn.stowage.plugin.Constants
import semele.quinn.stowage.plugin.Versions

plugins {
    id("java-library")
    kotlin("jvm")
    id("dev.yumi.gradle.licenser")
    id("dev.architectury.loom")
}

group = "semele.quinn"
version = "${Versions.stowage}+${project.name}"
base.archivesName = Constants.modIdentifier

layout.buildDirectory = rootProject.file("projects/${project.name}/build")

loom {
    silentMojangMappingsLicense()
}

repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Unofficial CurseForge Maven"
                url = uri("https://cursemaven.com/")
            }
        }
        filter { includeGroup("curse.maven") }
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth Maven"
                url = uri("https://api.modrinth.com/maven/")
            }
        }
        filter { includeGroup("maven.modrinth") }
    }

    maven {
        name = "ParchmentMC Maven"
        url = uri("https://maven.parchmentmc.org/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${Versions.minecraft}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${Versions.parchment}")
    })
}

tasks {
    named<ProcessResources>("processResources") {
        inputs.properties(mutableMapOf("version" to Versions.stowage))

        filesMatching(listOf("fabric.mod.json", "quilt.mod.json", "META-INF/mods.toml")) {
            expand(inputs.properties)
        }
    }
}

license {
    rule(rootProject.file("codeformat/license_header.txt"))
}