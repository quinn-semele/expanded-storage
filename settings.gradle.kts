pluginManagement {
    repositories {
        maven {
            name = "Fabric Maven"
            url = uri("https://maven.fabricmc.net/")
        }

        gradlePluginPortal()
    }
}

rootProject.name = "expandedstorage"

include(
        "common",
//        "thread",
//            "fabric",
//            "quilt",
        "forge"
)

project(":forge").name = "NeoForge"
