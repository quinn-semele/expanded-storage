import org.gradle.kotlin.dsl.*
import semele.quinn.expandedstorage.plugin.new_dependency.ModDependencies

plugins {
    id("java-library")
    id("dev.architectury.loom")
}

val modDependencies = ModDependencies().apply {
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
        val flkVersion = "1.10.16+kotlin.1.9.21" // https://modrinth.com/mod/fabric-language-kotlin/versions
        val qklVersion = "2.1.3+kt.1.9.10+flk.1.10.10" // https://modrinth.com/mod/qkl/versions
        implementation("maven.modrinth:inventory-profiles-next:$ipnVersion")
        implementation("maven.modrinth:libipn:$libIpnVersion")

        if (project.name == "quilt") {
            implementation("org.quiltmc.quilt-kotlin-libraries:quilt-kotlin-libraries:$qklVersion")
        } else {
            implementation("net.fabricmc:fabric-language-kotlin:$flkVersion")
        }
    }

    add("jei") {
        val jeiMcVersion = "1.20.1"
        val jeiVersion = "15.2.0.27" // https://modrinth.com/mod/jei/versions
        compileOnly("mezz.jei:jei-$jeiMcVersion-common-api:$jeiVersion")
        compileOnly("mezz.jei:jei-$jeiMcVersion-fabric-api:$jeiVersion")
        runtimeOnly("mezz.jei:jei-$jeiMcVersion-fabric:$jeiVersion")
    }

    add("modmenu", curseforgeDependencyName = null) {
        val modmenuVersion = "7.2.2" // https://modrinth.com/mod/modmenu/versions
        implementation("com.terraformersmc:modmenu:$modmenuVersion")
    }

    add("rei", curseforgeDependencyName = "roughly-enough-items") {
        val reiVersion = "12.0.684" // https://modrinth.com/mod/rei/versions
        implementation("me.shedaniel:RoughlyEnoughItems-fabric:$reiVersion")
    }

    add("towelette") {
        val toweletteVersion = "5.0.0+1.14.4-1.19.3" // https://modrinth.com/mod/towelette/versions
        val statementVersion = "4.2.5+1.14.4-1.19.3" // https://modrinth.com/mod/statement/versions
        implementation("maven.modrinth:towelette:$toweletteVersion")
        implementation("maven.modrinth:statement:$statementVersion")
    }

    add("inventory-tabs", curseforgeDependencyName = null) {
        val inventoryTabsVersion = "1.1.1+1.20" // https://modrinth.com/mod/inventory-tabs/versions
        implementation("folk.sisby:inventory-tabs:$inventoryTabsVersion")
    }
}

modDependencies.enableMods()

project.extra["mod_dependencies"] = modDependencies

repositories {
    maven { // Cardinal Components
        name = "Ladysnake maven"
        url = uri("https://maven.ladysnake.org/releases")
        content {
            includeGroup("dev.onyxstudios.cardinal-components-api")
        }
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "ARRP"
                url = uri("https://ueaj.dev/maven")
            }
        }
        filter {
            includeGroup("net.devtech")
        }
    }

    exclusiveContent { // Mod Menu
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

    exclusiveContent {// Inventory Tabs
        forRepository {
            maven {
                name = "Sleeping Town Maven"
                url = uri("https://repo.sleeping.town/")
            }
        }
        filter {
            includeGroup("folk.sisby")
        }
    }

    maven { // Quark, JEI
        name = "Jared"
        url = uri("https://maven.blamejared.com/")
    }

    maven { // Roughly Enough Items
        name = "Shedaniel"
        url = uri("https://maven.shedaniel.me/")
    }

    maven { // Amecs
        name = "Siphalor's Maven"
        url = uri("https://maven.siphalor.de/")
    }
}

dependencies {
    modDependencies.iterateCompileDependencies { dependency ->
        add("modCompileOnly", dependency) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }

    modDependencies.iterateRuntimeDependencies { dependency ->
        add("modRuntimeOnly", dependency) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }
}
