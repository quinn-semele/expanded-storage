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

    forge {
        mixinConfig("expandedstorage-common.mixins.json")
        mixinConfig("expandedstorage-forge.mixins.json")

        convertAccessWideners = true
    }

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

tasks.getByName<Jar>("jar") {
    manifest.attributes(mapOf(
            "Automatic-Module-Name" to "ellemes.expandedstorage",
            "MixinConfigs" to loom.forge.mixinConfigs.get().joinToString(",")
    ))
}

val modDependencies = FreezableDependencyList().apply {
    from(project(":common").extra["mod_dependencies"])

    add("emi") {
        val emiVersion = "1.1.3+1.20.1" // https://modrinth.com/mod/emi/versions

        compileOnly("dev.emi:emi-forge:${emiVersion}:api")
        runtimeOnly("dev.emi:emi-forge:${emiVersion}")
    }

    add("jei") {
        val minecraftVersion = "1.20.1"
        val version = "15.3.0.4"

        compileOnly("mezz.jei:jei-$minecraftVersion-forge-api:$version")
        runtimeOnly("mezz.jei:jei-$minecraftVersion-forge:$version")
    }

    add("quark") {
        val zetaVersion = "1.0-14.69"
        val quarkVersion = "4.0-437.3290"

        implementation("org.violetmoon.quark:Quark:$quarkVersion")
        runtimeOnly("org.violetmoon.zeta:Zeta:$zetaVersion")
    }

    add("inventory-profiles-next") {
        val libVersion = "4.0.1"
        val libMinecraftVersion = "1.20"
        val minecraftVersion = "1.20.1"
        val version = "1.10.9"

        implementation("maven.modrinth:inventory-profiles-next:forge-$minecraftVersion-$version")
        implementation("maven.modrinth:libipn:forge-$libMinecraftVersion-$libVersion")
        implementation("maven.modrinth:kotlin-for-forge:4.10.0")
    }

    add("rei", cfDependencyName = "roughly-enough-items") {
        val version = "12.0.684"

        compileOnly("me.shedaniel:RoughlyEnoughItems-api:$version")
        compileOnly("me.shedaniel:RoughlyEnoughItems-api-forge:$version")
        runtimeOnly("me.shedaniel:RoughlyEnoughItems-forge:$version")
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
    forge("net.minecraftforge:forge:${Versions.MINECRAFT}-${Versions.FORGE}")

    modDependencies.compileDependencies(project).forEach(::modCompileOnly)
    modDependencies.runtimeDependencies(project).forEach(::modRuntimeOnly)
}
