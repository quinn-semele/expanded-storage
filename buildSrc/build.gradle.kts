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
                name = "FabricMC's Maven"
                url = uri("https://maven.fabricmc.net/")
            }
        }
        filter {
            includeGroup("net.fabricmc")
        }
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "QuiltMC's Release Maven"
                url = uri("https://maven.quiltmc.org/repository/release/")
            }
        }
        filter {
            includeGroup("org.quiltmc")
            includeGroup("org.quiltmc.loom")
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
    implementation(group = "org.quiltmc.loom", name = "org.quiltmc.loom.gradle.plugin", version = "1.7.4") // https://quiltmc.org/en/usage/latest-versions/
    implementation(group = "com.google.code.gson", name = "gson", version = "2.11.0")
}