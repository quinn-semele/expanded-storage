import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()

    exclusiveContent {
        forRepository {
            maven {
                name = "Fabric"
                url = uri("https://maven.fabricmc.net")
            }
        }
        filter {
            includeGroup("net.fabricmc")
            includeGroup("fabric-loom")
        }
    }
}

kotlin {
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_0
        apiVersion = KotlinVersion.KOTLIN_2_0
    }
}

dependencies {
    implementation(group = "net.neoforged", name = "moddev-gradle", version = "1.0.15") // https://projects.neoforged.net/neoforged/moddevgradle/
    implementation(group = "fabric-loom", name = "fabric-loom.gradle.plugin", version = "1.7-SNAPSHOT") // https://fabricmc.net/develop/
    implementation(group = "com.google.code.gson", name = "gson", version = "2.11.0")
}