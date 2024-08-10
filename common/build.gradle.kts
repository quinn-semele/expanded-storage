import dev.compasses.expandedstorage.ModVersions

plugins {
    id("multiloader-common")
}

repositories {
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

    maven { // Quark, JEI
        name = "Jared"
        url = uri("https://maven.blamejared.com/")
    }
}

dependencies {
    implementation("dev.emi:emi-xplat-mojmap:${ModVersions.EMI}:api")
    implementation("mezz.jei:jei-${ModVersions.JEI_GAME}-common-api:${ModVersions.JEI_MOD}")
    implementation("maven.modrinth:carry-on:${ModVersions.CARRY_ON_NEOFORGE}")
}
