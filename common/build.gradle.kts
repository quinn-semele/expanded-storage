import dev.compasses.expandedstorage.ModVersions

plugins {
    id("multiloader-common")
}

multiloader {
    mods {
        create("emi") {
            requiresRepo("TerraformersMC's Maven", "https://maven.terraformersmc.com/", setOf(
                "dev.emi"
            ))

            artifacts {
                compileOnly("dev.emi:emi-xplat-mojmap:${ModVersions.EMI}:api")
            }
        }

        create("jei") {
            requiresRepo("Jared's Maven", "https://maven.blamejared.com/", setOf(
                "mezz.jei"
            ))

            artifacts {
                compileOnly("mezz.jei:jei-${ModVersions.JEI_GAME}-common-api:${ModVersions.JEI_MOD}")
            }
        }

        create("carry-on") {
            artifacts {
                compileOnly("maven.modrinth:carry-on:${ModVersions.CARRY_ON_NEOFORGE}")
            }
        }
    }
}
