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

//        maven {
//            name = "Quilt Release Maven"
//            url = uri("https://maven.quiltmc.org/repository/release/")
//        }

//        maven {
//            name = "Quilt Snapshot Maven"
//            url = uri("https://maven.quiltmc.org/repository/snapshot/")
//        }

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