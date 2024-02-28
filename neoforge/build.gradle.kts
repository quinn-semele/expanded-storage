import semele.quinn.stowage.plugin.Constants
import semele.quinn.stowage.plugin.Versions

plugins {
    id("dev.architectury.loom")
    id("stowage-generic")
    id("stowage-common-dependent")
}

loom {
    runs {
        named("client") {
            property("neoforge.enabledGameTestNamespaces", Constants.modIdentifier)
        }

        named("server") {
            property("neoforge.enabledGameTestNamespaces", Constants.modIdentifier)
        }

        create("datagen") {
            data()

            programArgs(
                "--mod", Constants.modIdentifier,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath,
                "--existing", project(":common").file("src/main/resources/").absolutePath
            )

            runDir("build/datagen")
        }
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources/")
        }
    }
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

tasks {
    getByName<Jar>(JavaPlugin.JAR_TASK_NAME) {
        exclude(".cache/**")
    }

    getByName<Delete>(LifecycleBasePlugin.CLEAN_TASK_NAME) {
        delete(file("src/generated/resources/"))
    }
}