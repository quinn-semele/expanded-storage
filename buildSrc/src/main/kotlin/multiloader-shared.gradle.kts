import dev.compasses.multiloader.Constants
import dev.compasses.multiloader.extension.MultiLoaderExtension

plugins {
    `java-library`
}

group = Constants.GROUP
version = Constants.MOD_VERSION

base.archivesName = "${Constants.MOD_ID}-${project.name}-${Constants.MINECRAFT_VERSION}"

extensions.create("multiloader", MultiLoaderExtension::class, project)

java {
    toolchain.languageVersion = Constants.JAVA_VERSION
}

repositories {
    mavenCentral()

    exclusiveContent {
        forRepository {
            maven {
                name = "Sponge"
                url = uri("https://repo.spongepowered.org/repository/maven-public/")
            }
        }
        filter { includeGroupAndSubgroups("org.spongepowered") }
    }

    exclusiveContent {
        forRepositories(
            maven {
                name = "ParchmentMC"
                url = uri("https://maven.parchmentmc.org/")
            },
            maven {
                name = "NeoForge"
                url = uri("https://maven.neoforged.net/releases/")
            }
        )
        filter { includeGroup("org.parchmentmc.data") }
    }

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
}

dependencies {
    compileOnly("org.jetbrains:annotations:${Constants.JETBRAIN_ANNOTATIONS_VERSION}")
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Specification-Title" to Constants.MOD_NAME,
            "Specification-Vendor" to Constants.AUTHORS.firstEntry().key,
            "Specification-Version" to archiveVersion,
            "Implementation-Title" to project.name,
            "Implementation-Version" to archiveVersion,
            "Implementation-Vendor" to Constants.AUTHORS.firstEntry().key,
            "Built-On-Minecraft" to Constants.MINECRAFT_VERSION
        ))
    }
}

tasks.processResources {
    val replacements = mutableMapOf(
        "version" to version,
        "group" to Constants.GROUP,
        "mod_name" to Constants.MOD_NAME,
        "mod_id" to Constants.MOD_ID,
        "license" to Constants.LICENSE,
        "description" to Constants.DESCRIPTION,

        "fl_authors" to Constants.AUTHORS.keys.joinToString("\", \""),
        "ql_authors" to Constants.AUTHORS.map { """${it.key}": "${it.value}""" }.joinToString("\",\n\""),
        "nf_authors" to Constants.AUTHORS.keys.joinToString(","),

        "credits" to Constants.CREDITS.map { "${it.key} - ${it.value}" }.joinToString(",\n"),

        "java_version" to Constants.JAVA_VERSION.asInt(),
        "minecraft_version" to Constants.MINECRAFT_VERSION,
        "fl_minecraft_constraint" to Constants.FL_MINECRAFT_CONSTRAINT,
        "nf_minecraft_constraint" to Constants.NF_MINECRAFT_CONSTRAINT,

        "fabric_loader_version" to Constants.FABRIC_LOADER_VERSION,
        "fabric_api_version" to Constants.FABRIC_API_VERSION,

        "quilt_loader_version" to Constants.QUILT_LOADER_VERSION,
        "quilt_api_version" to Constants.QUILT_API_VERSION,

        "neoforge_version" to Constants.NEOFORGE_VERSION,
        "fml_version_constraint" to Constants.FML_CONSTRAINT,
    )

    inputs.properties(replacements)
    filesMatching(listOf("fabric.mod.json", "quilt.mod.json", "META-INF/neoforge.mods.toml", "*.mixins.json", "*.mcmeta")) {
        expand(replacements)
    }

    exclude(".cache/*")
}
