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
    implementation("dev.architectury:architectury-loom:1.5.388") // https://maven.architectury.dev/dev/architectury/architectury-loom/

    implementation("me.modmuss50:mod-publish-plugin:0.5.1") // https://plugins.gradle.org/plugin/me.modmuss50.mod-publish-plugin

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22") // Should match version of FLK / KFF

    implementation("dev.yumi:yumi-gradle-licenser:1.1.1") // https://plugins.gradle.org/plugin/dev.yumi.gradle.licenser

    implementation("org.jetbrains:annotations:24.1.0") // https://mvnrepository.com/artifact/org.jetbrains/annotations

    implementation("com.google.code.gson:gson:2.10.1") // https://mvnrepository.com/artifact/com.google.code.gson/gson
}