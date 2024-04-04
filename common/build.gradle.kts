import semele.quinn.expandedstorage.plugin.Versions
import semele.quinn.expandedstorage.plugin.dependency.FreezableDependencyList

plugins {
    id("expandedstorage-generic")
}

loom {
    accessWidenerPath = file("src/main/resources/expandedstorage.accessWidener")
}

val modDependencies = FreezableDependencyList().apply {
    add("emi") {
        if (it.name != "common") return@add // Idk am getting some funky class errors on forge.

        compileOnly("dev.emi:emi-xplat-intermediary:${Versions.EMI}:api")
    }

    add("jei") {
        compileOnly("mezz.jei:jei-${Versions.JEI_MINECRAFT}-common-api:${Versions.JEI}")
    }

    add("carry-on") {
        compileOnly("maven.modrinth:carry-on:${Versions.CARRY_ON}")
    }

    add("inventory-profiles-next") {
        if (it.name != "common") return@add // IPN doesn't have an easily accessible common api.

        compileOnly("maven.modrinth:inventory-profiles-next:fabric-${Versions.IPN_MINECRAFT_FABRIC}-${Versions.IPN}")
    }

    freeze()
}

extra["mod_dependencies"] = modDependencies

repositories {
    maven { // Quark, JEI
        name = "Jared"
        url = uri("https://maven.blamejared.com/")
    }

    maven { // Roughly Enough Items
        name = "Shedaniel"
        url = uri("https://maven.shedaniel.me/")
    }

    exclusiveContent { // EMI
        forRepository {
            maven {
                name = "TerraformersMC"
                url = uri("https://maven.terraformersmc.com/")
            }
        }
        filter {
            includeGroup("dev.emi")
        }
    }
}

dependencies {
    modRuntimeOnly(modCompileOnly("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")!!)

    modDependencies.compileDependencies(project).forEach(::modCompileOnly)
}
