pluginManagement {
    repositories {
        maven {
            name = "Fabric Maven"
            url = uri("https://maven.fabricmc.net/")
        }

        maven {
            name = "Architectury Maven"
            url = uri("https://maven.architectury.dev/")
        }

        maven {
            name = "NeoForge Maven"
            url = uri("https://maven.neoforged.net/releases/")
        }

        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "stowage"

include(
    ":common",
    ":thread",
    ":fabric",
    ":quilt",
    ":neoforge"
)