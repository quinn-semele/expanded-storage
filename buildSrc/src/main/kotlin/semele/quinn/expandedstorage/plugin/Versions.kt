package semele.quinn.expandedstorage.plugin

import org.gradle.api.JavaVersion

object Versions {
    const val EXPANDEDSTORAGE = "8.4.0-beta.1"

    // Generic
    val JAVA = JavaVersion.VERSION_17
    const val MINECRAFT = "1.19.2"
    const val PARCHMENT = "1.19.2:2022.11.27"
    const val JETBRAINS_ANNOTATIONS_VERSION = "24.1.0"
    val CF_SUPPORTED_GAME_VERSIONS: List<String> = listOf(MINECRAFT)
    val MR_SUPPORTED_GAME_VERSIONS: List<String> = listOf(MINECRAFT)

    // Fabric
    const val FABRIC_LOADER = "0.14.10"
    const val FABRIC_API = "0.77.0+1.19.2" // https://modrinth.com/mod/fabric-api/
    const val FABRIC_KOTLIN = "1.10.16+kotlin.1.9.21" // https://modrinth.com/mod/fabric-language-kotlin/

    // Forge
    const val FORGE = "43.1.0"

    // Quilt
    const val QUILT_LOADER = "0.20.2"
    const val QUILT_API = "4.0.0-beta.30+0.77.0-1.19.2"
    const val QUILT_KOTLIN = "1.0.2+kt.1.8.0+flk.1.9.0" // https://modrinth.com/mod/qkl/

    // Dependencies
    const val EMI = "1.1.3+1.19.2" // https://modrinth.com/mod/emi/

    const val REI = "9.2.710" // https://modrinth.com/mod/rei/

    const val JEI = "11.4.0.286" // https://modrinth.com/mod/jei/
    const val JEI_MINECRAFT = MINECRAFT

    const val IPN = "1.10.9" // https://modrinth.com/mod/inventory-profiles-next/
    const val IPN_MINECRAFT_FABRIC = MINECRAFT
    const val IPN_MINECRAFT_FORGE = MINECRAFT

    const val LIB_IPN = "4.0.1" // https://modrinth.com/mod/libipn/
    const val LIB_IPN_MINECRAFT = MINECRAFT

    const val KFF = "3.12.0" // https://modrinth.com/mod/kotlin-for-forge/

    const val INVENTORY_TABS = "1.1.8+1.19" // https://modrinth.com/mod/inventory-tabs/

    const val MOD_MENU = "4.1.2" // https://modrinth.com/mod/modmenu/

    const val HTM = "1.1.6" // https://modrinth.com/mod/htm/

    const val AMECS = "1.3.10+mc.1.19-rc2" // https://maven.siphalor.de/de/siphalor/amecs-1.19/
    const val AMECS_API = "1.5.3+mc22w17a" // https://maven.siphalor.de/de/siphalor/amecsapi-1.19/
    const val AMECS_MINECRAFT = "1.19"

    const val CARRIER = "1.12.0" // https://modrinth.com/mod/carrier/
    const val CARDINAL_COMPONENTS = "5.0.2" // https://modrinth.com/mod/cardinal-components-api/
    const val ARRP = "0.6.4" // https://modrinth.com/mod/arrp/

    const val CARRY_ON_FABRIC = "J7qT9hJD" // https://modrinth.com/mod/carry-on/
    const val CARRY_ON_FORGE = "CE3MquDi" // https://modrinth.com/mod/carry-on/

    const val AUTO_REG_LIB = "1.8.2-55" // https://modrinth.com/mod/autoreglib/
    const val QUARK = "3.4-408.3077" // https://modrinth.com/mod/quark/
}
