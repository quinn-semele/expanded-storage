pluginManagement {
    repositories {
        maven {
            name = "Fabric Maven"
            url = uri("https://maven.fabricmc.net/")
        }

        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "expanded-storage"

include(
        "common",
//        "thread",
//            "fabric",
//            "quilt",
        "neoforge"
)

project(":neoforge").name = "NeoForge"
