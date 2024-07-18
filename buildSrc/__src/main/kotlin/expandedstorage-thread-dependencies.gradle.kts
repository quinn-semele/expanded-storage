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
