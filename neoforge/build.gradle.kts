import semele.quinn.stowage.plugin.Versions

plugins {
    id("dev.architectury.loom")
    id("stowage-generic")
    id("stowage-common-dependent")
}

repositories {
    maven {
        name = "NeoForge Maven"
        url = uri("https://maven.neoforged.net/releases/")
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "Kotlin For Forge Maven"
                url = uri("https://thedarkcolour.github.io/KotlinForForge/")
            }
        }
        filter { includeGroup("thedarkcolour") }
    }
}

dependencies {
    neoForge("net.neoforged:neoforge:${Versions.neoforge}")

    implementation("thedarkcolour:kotlinforforge-neoforge:${Versions.kotlinForForge}")
}