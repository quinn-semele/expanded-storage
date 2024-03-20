import semele.quinn.expandedstorage.plugin.dependency.DependencyHelper
import semele.quinn.expandedstorage.plugin.dependency.ModPlatform
import semele.quinn.expandedstorage.plugin.dependency.Mods
import kotlin.reflect.KClass

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
    maven { // Quark
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

// Note: when changing this you will likely need to stop any gradle deamons and delete the root .gradle folder.
val enabledMods = setOf<KClass<*>>()

val platform = when (project.name) {
    "common" -> ModPlatform.Common
    "fabric" -> ModPlatform.Fabric
    "forge" -> ModPlatform.Forge
    "thread" -> ModPlatform.Thread
    "quilt" -> ModPlatform.Quilt
    else -> throw IllegalArgumentException()
}

dependencies {
    val helper = DependencyHelper(
            this,
            project.providers
    )

    Mods::class.nestedClasses.forEach {
        val mod = it.constructors.first().call(platform, helper) as Mods

        mod.applyCompileDependencies()
        if (mod::class in enabledMods) {
            mod.applyRuntimeDependencies()
        }
    }
}
