import dev.compasses.expandedstorage.ModVersions

plugins {
    id("multiloader-thread")
}

repositories {
    maven { // Cardinal Components
        name = "Ladysnake maven"
        url = uri("https://maven.ladysnake.org/releases")
        content {
            includeGroup("org.ladysnake.cardinal-components-api")
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

    exclusiveContent {
        forRepository {
            maven {
                name = "JitPack"
                url = uri("https://jitpack.io/")
            }
        }
        filter {
            includeGroup("com.github.Virtuoel")
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


multiloader {
    dependencies {
        create("inventory-tabs") {
            curseforgeName.unsetConvention()

            artifacts { enabled ->
                ifEnabled(enabled, "folk.sisby:inventory-tabs:${ModVersions.INVENTORY_TABS}")
            }
        }

        create("rei") {
            curseforgeName = "roughly-enough-items"

            artifacts { enabled ->
                ifEnabled(enabled, "me.shedaniel:RoughlyEnoughItems-fabric:${ModVersions.REI}")
            }
        }

        create("modmenu") {
            artifacts { enabled ->
                ifEnabled(enabled, "com.terraformersmc:modmenu:${ModVersions.MOD_MENU}")
            }
        }

        create("jei") {
            artifacts { enabled ->
                modCompileOnly("mezz.jei:jei-${ModVersions.JEI_GAME}-fabric-api:${ModVersions.JEI_MOD}")
                if (enabled) {
                    modRuntimeOnly("mezz.jei:jei-${ModVersions.JEI_GAME}-fabric:${ModVersions.JEI_MOD}")
                }
            }
        }

        create("inventory-profiles-next") {
            artifacts { enabled ->
                ifEnabled(enabled, "maven.modrinth:inventory-profiles-next:fabric-${ModVersions.IPN_GAME}-${ModVersions.IPN_MOD}")
                ifEnabled(enabled, "maven.modrinth:libipn:fabric-${ModVersions.LIB_IPN_GAME_FABRIC}-${ModVersions.LIB_IPN_MOD}")
            }
        }

        create("htm") {
            artifacts {  enabled ->
                ifEnabled(enabled, "maven.modrinth:htm:${ModVersions.HTM}")
            }
        }

        create("emi") {
            artifacts { enabled ->
                modCompileOnly("dev.emi:emi-fabric:${ModVersions.EMI}:api")
                if (enabled) {
                    modRuntimeOnly("dev.emi:emi-fabric:${ModVersions.EMI}")
                }
            }
        }

        create("carry-on") {
            artifacts { enabled ->
                ifEnabled(enabled, "maven.modrinth:carry-on:${ModVersions.CARRY_ON_FABRIC}")
            }
        }

        create("amecs") {
            artifacts { enabled ->
                modCompileOnly("de.siphalor:amecsapi-${ModVersions.AMECS_GAME}:${ModVersions.AMECS_API_MOD}")
                if (enabled) {
                    modRuntimeOnly("de.siphalor:amecs-${ModVersions.AMECS_GAME}:${ModVersions.AMECS_MOD}")
                }
            }
        }

        create("carrier") {
            artifacts { enabled ->
                ifEnabled(enabled, "maven.modrinth:carrier:${ModVersions.CARRIER}")
                ifEnabled(enabled, "org.ladysnake.cardinal-components-api:cardinal-components-base:${ModVersions.CARDINAL_COMPONENTS}")
                ifEnabled(enabled, "org.ladysnake.cardinal-components-api:cardinal-components-entity:${ModVersions.CARDINAL_COMPONENTS}")
                ifEnabled(enabled, "net.devtech:arrp:${ModVersions.ARRP}")
            }
        }
    }
}

fun DependencyHandler.ifEnabled(enabled: Boolean, dependencyNotation: String) {
    if (enabled) {
        modImplementation(dependencyNotation)
    } else {
        modCompileOnly(dependencyNotation)
    }
}
