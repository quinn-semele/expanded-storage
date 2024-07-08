package semele.quinn.expandedstorage.plugin

import org.gradle.api.JavaVersion

object Versions {
    const val EXPANDEDSTORAGE = "14.0.1"

    // Generic
    val JAVA = JavaVersion.VERSION_21
    const val MINECRAFT = "1.21"
    const val PARCHMENT = "1.20.6:2024.06.16"
    const val JETBRAINS_ANNOTATIONS_VERSION = "24.1.0"
    val CF_SUPPORTED_GAME_VERSIONS: List<String> = listOf(MINECRAFT)
    val MR_SUPPORTED_GAME_VERSIONS: List<String> = listOf(MINECRAFT)

    // Fabric
    const val FABRIC_LOADER = "0.15.11"
    const val FABRIC_API = "0.99.4+1.21"
    const val FABRIC_KOTLIN = "1.11.0+kotlin.2.0.0" // https://modrinth.com/mod/fabric-language-kotlin/

    // NeoForge
    const val NEOFORGE = "21.0.40-beta"

    // Quilt
    const val QUILT_LOADER = "0.23.1"
    const val QUILT_API = "9.0.0-alpha.6+0.96.11-1.20.4"
    const val QUILT_KOTLIN = "4.0.0+kt.1.9.23+flk.1.10.19"

    // Dependencies
    const val EMI = "1.1.4+1.20.4" // https://modrinth.com/mod/emi/

    const val REI = "16.0.729" // https://modrinth.com/mod/rei/

    const val JEI = "17.3.0.49" // https://modrinth.com/mod/jei/
    const val JEI_MINECRAFT = "1.20.4"

    const val IPN = "1.10.10" // https://modrinth.com/mod/inventory-profiles-next/
    const val IPN_MINECRAFT_FABRIC = "1.20.2"

    const val LIB_IPN = "4.0.2" // https://modrinth.com/mod/libipn/
    const val LIB_IPN_MINECRAFT = "1.20.2"

    const val INVENTORY_TABS = "1.1.8+1.20" // https://modrinth.com/mod/inventory-tabs/

    const val MOD_MENU = "9.0.0" // https://modrinth.com/mod/modmenu/

    const val HTM = "1.1.11" // https://modrinth.com/mod/htm/

    const val AMECS = "1.3.11+mc.1.20.4" // https://maven.siphalor.de/de/siphalor/amecs-1.20/
    const val AMECS_API = "1.5.6+mc1.20.2" // https://maven.siphalor.de/de/siphalor/amecsapi-1.20/
    const val AMECS_MINECRAFT = "1.20"

    const val CARRIER = "1.12.0" // https://modrinth.com/mod/carrier/
    const val CARDINAL_COMPONENTS = "5.4.0" // https://modrinth.com/mod/cardinal-components-api/
    const val ARRP = "0.8.1" // https://modrinth.com/mod/arrp/

    const val CARRY_ON_FABRIC = "EvDx8gEe" // https://modrinth.com/mod/carry-on/
    const val CARRY_ON_FORGE = "8a6KfB5j" // https://modrinth.com/mod/carry-on/

    const val ZETA = "1.0-14.69" // https://modrinth.com/mod/zeta/
    const val QUARK = "4.0-437.3290" // https://modrinth.com/mod/quark/
}
