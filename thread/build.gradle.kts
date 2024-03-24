import semele.quinn.expandedstorage.plugin.Constants
import semele.quinn.expandedstorage.plugin.Versions
import semele.quinn.expandedstorage.plugin.dependency.FreezableDependencyList

plugins {
    id("expandedstorage-generic")
    id("expandedstorage-common-dependent")
    id("expandedstorage-thread-dependencies")
}

loom {
    accessWidenerPath = project(":common").loom.accessWidenerPath
}

val modDependencies = FreezableDependencyList().apply {
    from(project(":common").extra["mod_dependencies"])

    add("amecs") {
        compileOnly("de.siphalor:amecsapi-${Versions.AMECS_MINECRAFT}:${Versions.AMECS_API}")
        runtimeOnly("de.siphalor:amecs-${Versions.AMECS_MINECRAFT}:${Versions.AMECS}")
    }

    add("carrier") {
        implementation("maven.modrinth:carrier:${Versions.CARRIER}")
        implementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${Versions.CARDINAL_COMPONENTS}")
        implementation("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${Versions.CARDINAL_COMPONENTS}")
        implementation("net.devtech:arrp:${Versions.ARRP}")
    }

    add("emi") {
        compileOnly("dev.emi:emi-fabric:${Versions.EMI}:api")
        runtimeOnly("dev.emi:emi-fabric:${Versions.EMI}")
    }

    add("htm") {
        implementation("maven.modrinth:htm:${Versions.HTM}")
    }

    add("inventory-profiles-next") {
        implementation("maven.modrinth:inventory-profiles-next:fabric-${Versions.IPN_MINECRAFT_FABRIC}-${Versions.IPN}")
        implementation("maven.modrinth:libipn:fabric-${Versions.LIB_IPN_MINECRAFT}-${Versions.LIB_IPN}")
    }

    add("jei") {
        compileOnly("mezz.jei:jei-${Versions.JEI_MINECRAFT}-fabric-api:${Versions.JEI}")
        runtimeOnly("mezz.jei:jei-${Versions.JEI_MINECRAFT}-fabric:${Versions.JEI}")
    }

    add("modmenu", cfDependencyName = null) {
        implementation("com.terraformersmc:modmenu:${Versions.MOD_MENU}")
    }

    add("rei", cfDependencyName = "roughly-enough-items") {
        implementation("me.shedaniel:RoughlyEnoughItems-fabric:${Versions.REI}")
    }

    add("towelette") {
        implementation("maven.modrinth:towelette:${Versions.TOWELETTE}")
        implementation("maven.modrinth:statement:${Versions.STATEMENT}")
    }

    add("inventory-tabs", cfDependencyName = null) {
        implementation("folk.sisby:inventory-tabs:${Versions.INVENTORY_TABS}")
    }

    freeze()
}

project.extra["mod_dependencies"] = modDependencies

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")

    modDependencies.compileDependencies(project).forEach {
        modCompileOnly(it) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }

    modDependencies.runtimeDependencies(project).forEach {
        modRuntimeOnly(it) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }
}

fabricApi.configureDataGeneration {
    outputDirectory = file("src/generated/resources")
    modId = Constants.MOD_ID
}
