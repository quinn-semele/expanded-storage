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
        val amecsMcVersion = "1.20"
        val amecsApiVersion = "1.3.9+mc1.20-pre1" // https://maven.siphalor.de/de/siphalor/amecsapi-1.20/
        val amecsVersion = "1.3.10+mc.1.20.1" // https://maven.siphalor.de/de/siphalor/amecs-1.20/
        compileOnly("de.siphalor:amecsapi-$amecsMcVersion:$amecsApiVersion")
        runtimeOnly("de.siphalor:amecs-$amecsMcVersion:$amecsVersion")
    }

    add("carrier") {
        val carrierVersion = "1.12.0" // https://modrinth.com/mod/carrier/versions
        val cardinalComponentsVersion = "5.2.2" // https://modrinth.com/mod/cardinal-components-api/versions
        val arrpVersion = "0.8.0" // https://modrinth.com/mod/arrp/versions
        implementation("maven.modrinth:carrier:$carrierVersion")
        implementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:$cardinalComponentsVersion")
        implementation("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:$cardinalComponentsVersion")
        implementation("net.devtech:arrp:$arrpVersion")
    }

    add("emi") {
        val emiVersion = "1.0.28+1.20.1" // https://modrinth.com/mod/emi/versions
        compileOnly("dev.emi:emi-fabric:${emiVersion}:api")
        runtimeOnly("dev.emi:emi-fabric:${emiVersion}")
    }

    add("htm") {
        val htmVersion = "1.1.9" // https://modrinth.com/mod/htm/versions
        implementation("maven.modrinth:htm:$htmVersion")
    }

    add("inventory-profiles-next") {
        val ipnVersion = "fabric-1.20-1.10.9" // https://modrinth.com/mod/inventory-profiles-next/versions
        val libIpnVersion = "fabric-1.20-4.0.1" // https://modrinth.com/mod/libipn/versions
        implementation("maven.modrinth:inventory-profiles-next:$ipnVersion")
        implementation("maven.modrinth:libipn:$libIpnVersion")
    }

    add("jei") {
        val jeiMcVersion = "1.20.1"
        val jeiVersion = "15.2.0.27" // https://modrinth.com/mod/jei/versions
        compileOnly("mezz.jei:jei-$jeiMcVersion-fabric-api:$jeiVersion")
        runtimeOnly("mezz.jei:jei-$jeiMcVersion-fabric:$jeiVersion")
    }

    add("modmenu", cfDependencyName = null) {
        val modmenuVersion = "7.2.2" // https://modrinth.com/mod/modmenu/versions
        implementation("com.terraformersmc:modmenu:$modmenuVersion")
    }

    add("rei", cfDependencyName = "roughly-enough-items") {
        val reiVersion = "12.0.684" // https://modrinth.com/mod/rei/versions
        implementation("me.shedaniel:RoughlyEnoughItems-fabric:$reiVersion")
    }

    add("towelette") {
        val toweletteVersion = "5.0.0+1.14.4-1.19.3" // https://modrinth.com/mod/towelette/versions
        val statementVersion = "4.2.5+1.14.4-1.19.3" // https://modrinth.com/mod/statement/versions
        implementation("maven.modrinth:towelette:$toweletteVersion")
        implementation("maven.modrinth:statement:$statementVersion")
    }

    add("inventory-tabs", cfDependencyName = null) {
        val inventoryTabsVersion = "1.1.1+1.20" // https://modrinth.com/mod/inventory-tabs/versions
        implementation("folk.sisby:inventory-tabs:$inventoryTabsVersion")
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
