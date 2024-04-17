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

tasks.getByName<Jar>("minJar") {
    manifest.attributes(mapOf(
            "Automatic-Module-Name" to "ellemes.expandedstorage",
            "MixinConfigs" to loom.forge.mixinConfigs.get().joinToString(",")
    ))
}

val modDependencies = FreezableDependencyList().apply {
    from(project(":common").extra["mod_dependencies"])

    add("emi") {
        compileOnly("dev.emi:emi-forge:${Versions.EMI}:api")
        runtimeOnly("dev.emi:emi-forge:${Versions.EMI}")
    }

    add("jei") {
        compileOnly("mezz.jei:jei-${Versions.JEI_MINECRAFT}-forge-api:${Versions.JEI}")
        runtimeOnly("mezz.jei:jei-${Versions.JEI_MINECRAFT}-forge:${Versions.JEI}")
    }

    add("carry-on") {
        implementation("maven.modrinth:carry-on:${Versions.CARRY_ON_FORGE}")
    }

    add("quark") {
        implementation("vazkii.quark:Quark:${Versions.QUARK}")
        runtimeOnly("vazkii.autoreglib:AutoRegLib:${Versions.AUTO_REG_LIB}")
    }

    add("inventory-profiles-next") {
        implementation("maven.modrinth:inventory-profiles-next:forge-${Versions.IPN_MINECRAFT_FORGE}-${Versions.IPN}")
        implementation("maven.modrinth:libipn:forge-${Versions.LIB_IPN_MINECRAFT}-${Versions.LIB_IPN}")
        implementation("maven.modrinth:kotlin-for-forge:${Versions.KFF}")
    }

    add("rei", cfDependencyName = "roughly-enough-items") {
        compileOnly("me.shedaniel:RoughlyEnoughItems-api:${Versions.REI}")
        compileOnly("me.shedaniel:RoughlyEnoughItems-api-forge:${Versions.REI}")
        runtimeOnly("me.shedaniel:RoughlyEnoughItems-forge:${Versions.REI}")
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
