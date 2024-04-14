plugins {
    `kotlin-dsl`
}

repositories {
    maven {
        name = "Architectury Maven"
        url = uri("https://maven.architectury.dev/")
    }

    maven {
        name = "MinecraftForge Maven"
        url = uri("https://maven.minecraftforge.net/")
    }

    maven {
        name = "FabricMC Maven"
        url = uri("https://maven.fabricmc.net/")
    }

    gradlePluginPortal()
    mavenCentral() // For Kotlin
}

dependencies {
    implementation("dev.architectury:architectury-loom:1.6-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.10.1")
}
