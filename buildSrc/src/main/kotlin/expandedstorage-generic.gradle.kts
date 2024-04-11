import semele.quinn.expandedstorage.plugin.Constants
import semele.quinn.expandedstorage.plugin.Versions
import java.nio.charset.StandardCharsets

plugins {
    id("java-library")
    id("dev.architectury.loom")
}

group = "compasses"
version = "${Versions.EXPANDEDSTORAGE}+${Versions.MINECRAFT}"
base.archivesName = Constants.MOD_ID
project.layout.buildDirectory = rootProject.file("build/${project.name}")

loom {
    silentMojangMappingsLicense()

    runs {
        named("client") {
            ideConfigGenerated(false)
        }

        named("server") {
            ideConfigGenerated(false)
            serverWithGui()
        }
    }

    @Suppress("UnstableApiUsage")
    mixin {
        if (project.name == "forge") {
            useLegacyMixinAp = true
        }
        defaultRefmapName = "expandedstorage.refmap.json"
    }
}

java {
    sourceCompatibility = Versions.java
    targetCompatibility = Versions.java
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = StandardCharsets.UTF_8.name()
    options.release = Versions.java.ordinal + 1
}

tasks.withType<ProcessResources>().configureEach {
    val props = mutableMapOf(
            "version" to Versions.EXPANDEDSTORAGE
    )

    inputs.properties(props)
    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "quilt.mod.json")) { expand(props) }

    exclude(".cache/*")
}

tasks.jar {
    exclude("**/datagen")
}

tasks.remapJar {
    if (!loom.isForge) {
        injectAccessWidener.set(true)
    }
}

repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Unofficial CurseForge Maven"
                url = uri("https://cursemaven.com/")
            }
        }
        filter {
            includeGroup("curse.maven")
        }
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth Maven"
                url = uri("https://api.modrinth.com/maven/")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }

    maven {
        name = "ParchmentMC Maven"
        url = uri("https://maven.parchmentmc.org/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${Versions.MINECRAFT}")

    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${Versions.PARCHMENT}@zip")
    })

    compileOnly("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS_VERSION}")
}
