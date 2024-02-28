plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

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

dependencies {
    implementation("dev.architectury:architectury-loom:1.5.388")

    implementation("me.modmuss50:mod-publish-plugin:0.5.1")

    implementation("org.jetbrains:annotations:24.1.0")

    implementation("com.google.code.gson:gson:2.10.1")
}