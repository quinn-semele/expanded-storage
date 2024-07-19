plugins {
    id("fabric-loom") version "1.7-SNAPSHOT"
}

loom {
    accessWidenerPath = file("src/main/resources/expandedstorage.accessWidener")
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

    exclusiveContent { // Mod Menu, EMI
        forRepository {
            maven {
                name = "TerraformersMC"
                url = uri("https://maven.terraformersmc.com/")
            }
        }
        filter {
            includeGroup("com.terraformersmc")
            includeGroup("dev.emi")
        }
    }

    maven { // Quark, JEI
        name = "Jared"
        url = uri("https://maven.blamejared.com/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")

    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${properties["minecraft_version"]}:${properties["parchment_version"]}@zip")
    })

    compileOnly("org.jetbrains:annotations:${properties["jetbrains_annotation_version"]}")

    modRuntimeOnly(modCompileOnly("net.fabricmc:fabric-loader:${properties["fabric_loader_version"]}")!!)

    modCompileOnly("dev.emi:emi-xplat-intermediary:${properties["emi_version"]}:api")
    modCompileOnly("mezz.jei:jei-${properties["jei_minecraft_version"]}-common-api:${properties["jei_version"]}")
    modCompileOnly("maven.modrinth:carry-on:${properties["carry_on_fabric_version"]}")
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

artifacts {
    add("commonJava", sourceSets.main.get().java.sourceDirectories.singleFile)
    add("commonResources", sourceSets.main.get().resources.sourceDirectories.singleFile)
}
