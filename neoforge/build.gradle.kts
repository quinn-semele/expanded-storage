import dev.compasses.expandedstorage.ModVersions

plugins {
    id("multiloader-neoforge")
}

configurations {
    val localRuntime = create("localRuntime")
    runtimeClasspath.configure { extendsFrom(localRuntime) }
}

fun DependencyHandler.localRuntime(notation: Any) {
    add("localRuntime", notation)
}

multiloader {
    mods {
        create("emi") {
            requiresRepo("TerraformersMC's Maven", "https://maven.terraformersmc.com/", setOf(
                "dev.emi"
            ))

            artifacts { enabled ->
                compileOnly("dev.emi:emi-neoforge:${ModVersions.EMI}:api")
                if (enabled) {
                    localRuntime("dev.emi:emi-neoforge:${ModVersions.EMI}")
                }
            }
        }

        create("jei") {
            requiresRepo("Jared's Maven", "https://maven.blamejared.com/", setOf(
                "mezz.jei"
            ))

            artifacts { enabled ->
                compileOnly("mezz.jei:jei-${ModVersions.JEI_GAME}-neoforge-api:${ModVersions.JEI_MOD}")
                if (enabled) {
                    localRuntime("mezz.jei:jei-${ModVersions.JEI_GAME}-neoforge:${ModVersions.JEI_MOD}")
                }
            }
        }

        create("rei") {
            curseforgeName = "roughly-enough-items"

            requiresRepo("Shedaniel's Maven", "https://maven.shedaniel.me/", setOf(
                "me.shedaniel",
                "me.shedaniel.cloth",
                "dev.architectury"
            ))

            artifacts { enabled ->
                compileOnly("me.shedaniel.cloth:cloth-config-neoforge:${ModVersions.CLOTH_CONFIG}")
                compileOnly("me.shedaniel:RoughlyEnoughItems-api-neoforge:${ModVersions.REI}")
                if (enabled) {
                    localRuntime("me.shedaniel:RoughlyEnoughItems-neoforge:${ModVersions.REI}")
                }
            }
        }

        create("carry-on") {
            artifacts { enabled ->
                compileOnly("maven.modrinth:carry-on:${ModVersions.CARRY_ON_NEOFORGE}")
                if (enabled) {
                    localRuntime("maven.modrinth:carry-on:${ModVersions.CARRY_ON_NEOFORGE}")
                }
            }
        }

        create("quark") {
            requiresRepo("Jared's Maven", "https://maven.blamejared.com/", setOf(
                "org.violetmoon.quark",
                "org.violetmoon.zeta"
            ))

            artifacts { enabled ->
                compileOnly("org.violetmoon.quark:Quark:${ModVersions.QUARK}")
                if (enabled) {
                    localRuntime("org.violetmoon.quark:Quark:${ModVersions.QUARK}")
                    localRuntime("org.violetmoon.zeta:Zeta:${ModVersions.ZETA}")
                }
            }
        }
    }
}
