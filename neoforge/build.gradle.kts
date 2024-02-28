import semele.quinn.stowage.plugin.Versions

plugins {
    id("dev.architectury.loom")
    id("stowage-generic")
}

repositories {
    maven {
        name = "NeoForge Maven"
        url = uri("https://maven.neoforged.net/releases/")
    }
}

dependencies {
    neoForge("net.neoforged:neoforge:${Versions.neoforge}")
}