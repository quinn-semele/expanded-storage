plugins {
    id("multiloader-neoforge")
}

configurations {
    val localRuntime = create("localRuntime")
    runtimeClasspath.configure { extendsFrom(localRuntime) }
}

repositories {
    maven { // Quark, JEI
        name = "Jared"
        url = uri("https://maven.blamejared.com/")
    }

    maven { // Roughly Enough Items
        name = "Shedaniel"
        url = uri("https://maven.shedaniel.me/")
    }

    exclusiveContent { // EMI
        forRepository {
            maven {
                name = "TerraformersMC"
                url = uri("https://maven.terraformersmc.com/")
            }
        }
        filter {
            includeGroup("dev.emi")
        }
    }
}

fun DependencyHandlerScope.localRuntime(notation: Any) {
    add("localRuntime", notation)
}

fun DependencyHandlerScope.localRuntimeIf(check:Boolean, notation: Any) {
    if (check) {
        localRuntime(notation)
    }
}

val mods: Map<String, DependencyHandlerScope.(Boolean) -> Unit> = mapOf(
    "emi" to { enabled ->
        compileOnly("dev.emi:emi-neoforge:${properties["emi_version"]}:api")
        localRuntimeIf(enabled, "dev.emi:emi-neoforge:${properties["emi_version"]}")
    },
    "jei" to { enabled ->
        compileOnly("mezz.jei:jei-${properties["jei_minecraft_version"]}-neoforge-api:${properties["jei_version"]}")
        localRuntimeIf(enabled, "mezz.jei:jei-${properties["jei_minecraft_version"]}-neoforge:${properties["jei_version"]}")
    },
    "rei" to { enabled ->
        compileOnly("me.shedaniel.cloth:cloth-config-neoforge:${properties["cloth_config_version"]}")
        compileOnly("me.shedaniel:RoughlyEnoughItems-api-neoforge:${properties["rei_version"]}")
        localRuntimeIf(enabled, "me.shedaniel:RoughlyEnoughItems-neoforge:${properties["rei_version"]}")
    },
    "carry-on" to { enabled ->
        compileOnly("maven.modrinth:carry-on:${properties["carry_on_forge_version"]}")
        localRuntimeIf(enabled, "maven.modrinth:carry-on:${properties["carry_on_forge_version"]}")
    },
    "quark" to { enabled ->
        compileOnly("org.violetmoon.quark:Quark:${properties["quark_version"]}")
        localRuntimeIf(enabled, "org.violetmoon.quark:Quark:${properties["quark_version"]}")
        localRuntimeIf(enabled, "org.violetmoon.zeta:Zeta:${properties["zeta_version"]}")
    }
)

val enabledMods: List<String> = listOf()

dependencies {
    mods.forEach { (id, dependencyApplier) -> dependencyApplier.invoke(this, id in enabledMods) }
}

