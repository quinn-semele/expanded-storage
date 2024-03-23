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

        val emiVersion = "1.1.3+1.20.1" // https://modrinth.com/mod/emi/versions

        compileOnly("dev.emi:emi-xplat-intermediary:${emiVersion}:api")
    }

    add("jei") {
        val minecraftVersion = "1.20.1"
        val version = "15.3.0.4"

        compileOnly("mezz.jei:jei-$minecraftVersion-common-api:$version")
    }

    add("inventory-profiles-next") {
        if (it.name != "common") return@add // IPN doesn't have an easily accessible common api.

        val minecraftVersion = "1.20"
        val version = "1.10.9"

        compileOnly("maven.modrinth:inventory-profiles-next:fabric-$minecraftVersion-$version")
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
