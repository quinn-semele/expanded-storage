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
    dependencies {
        create("emi") {
            requiresRepo("TerraformersMC's Maven", "https://maven.terraformersmc.com/", setOf(
                "dev.emi"
            ))

            artifacts { enabled ->
                compileOnly("dev.emi:emi-neoforge:${properties["emi_version"]}:api")
                if (enabled) {
                    localRuntime("dev.emi:emi-neoforge:${properties["emi_version"]}")
                }
            }
        }

        create("jei") {
            requiresRepo("Jared's Maven", "https://maven.blamejared.com/", setOf(
                "mezz.jei"
            ))

            artifacts { enabled ->
                compileOnly("mezz.jei:jei-${properties["jei_minecraft_version"]}-neoforge-api:${properties["jei_version"]}")
                if (enabled) {
                    localRuntime("mezz.jei:jei-${properties["jei_minecraft_version"]}-neoforge:${properties["jei_version"]}")
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
                compileOnly("me.shedaniel.cloth:cloth-config-neoforge:${properties["cloth_config_version"]}")
                compileOnly("me.shedaniel:RoughlyEnoughItems-api-neoforge:${properties["rei_version"]}")
                if (enabled) {
                    localRuntime("me.shedaniel:RoughlyEnoughItems-neoforge:${properties["rei_version"]}")
                }
            }
        }

        create("carry-on") {
            artifacts { enabled ->
                compileOnly("maven.modrinth:carry-on:${properties["carry_on_forge_version"]}")
                if (enabled) {
                    localRuntime("maven.modrinth:carry-on:${properties["carry_on_forge_version"]}")
                }
            }
        }

        create("quark") {
            requiresRepo("Jared's Maven", "https://maven.blamejared.com/", setOf(
                "org.violetmoon.quark",
                "org.violetmoon.zeta"
            ))

            artifacts { enabled ->
                compileOnly("org.violetmoon.quark:Quark:${properties["quark_version"]}")
                if (enabled) {
                    localRuntime("org.violetmoon.quark:Quark:${properties["quark_version"]}")
                    localRuntime("org.violetmoon.zeta:Zeta:${properties["zeta_version"]}")
                }
            }
        }
    }
}
