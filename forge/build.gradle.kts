import semele.quinn.expandedstorage.plugin.Constants
import semele.quinn.expandedstorage.plugin.Versions
import semele.quinn.expandedstorage.plugin.dependency.FreezableDependencyList

plugins {
    id("expandedstorage-generic")
    id("expandedstorage-common-dependent")
    id("expandedstorage-release-project")
}

loom {
    accessWidenerPath = project(":common").loom.accessWidenerPath

    runs {
        create("datagen") {
            data()

            programArg("--existing")
            programArg(file("src/main/resources").absolutePath)

            programArg("--existing")
            programArg(project(":common").file("src/main/resources").absolutePath)


            programArg("--all")

            programArg("--mod")
            programArg(Constants.MOD_ID)

            programArg("--output")
            programArg(file("src/generated/resources").absolutePath)
        }
    }
}

sourceSets.main {
    resources.srcDir(file("src/generated/resources"))
}

tasks.getByName<Jar>("minJar") {
    manifest.attributes(mapOf(
            "Automatic-Module-Name" to "ellemes.expandedstorage"
    ))
}

val modDependencies = FreezableDependencyList().apply {
    from(project(":common").extra["mod_dependencies"])

    add("emi") {
        compileOnly("dev.emi:emi-neoforge:${Versions.EMI}:api")
        runtimeOnly("dev.emi:emi-neoforge:${Versions.EMI}")
    }

    add("jei") {
        compileOnly("mezz.jei:jei-${Versions.JEI_MINECRAFT}-neoforge-api:${Versions.JEI}")
        runtimeOnly("mezz.jei:jei-${Versions.JEI_MINECRAFT}-neoforge:${Versions.JEI}")
    }

    add("carry-on") {
        implementation("maven.modrinth:carry-on:${Versions.CARRY_ON_FORGE}")
    }

    add("quark") {
        implementation("org.violetmoon.quark:Quark:${Versions.QUARK}")
        runtimeOnly("org.violetmoon.zeta:Zeta:${Versions.ZETA}")
    }

    add("rei", cfDependencyName = "roughly-enough-items") {
        compileOnly("me.shedaniel:RoughlyEnoughItems-api:${Versions.REI}")
        compileOnly("me.shedaniel:RoughlyEnoughItems-api-neoforge:${Versions.REI}")
        runtimeOnly("me.shedaniel:RoughlyEnoughItems-neoforge:${Versions.REI}")
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

    maven {
        name = "NeoForge Maven"
        url = uri("https://maven.neoforged.net/releases/")
    }

    maven {
        name = "NeoForge PR Maven #787' "// https://github.com/neoforged/NeoForge/pull/787
        url = uri("https://prmaven.neoforged.net/NeoForge/pr787")

        content {
            includeModule("net.neoforged", "testframework")
            includeModule("net.neoforged", "neoforge")
        }
    }
}

dependencies {
    neoForge("net.neoforged:neoforge:${Versions.NEOFORGE}")

    modDependencies.compileDependencies(project).forEach(::modCompileOnly)
    modDependencies.runtimeDependencies(project).forEach(::modRuntimeOnly)
}

tasks.remapJar {
    atAccessWideners.add("expandedstorage.accessWidener")
}
