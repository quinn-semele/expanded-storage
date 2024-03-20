package semele.quinn.expandedstorage.plugin

import org.gradle.api.JavaVersion

object Versions {
    const val EXPANDEDSTORAGE = "10.3.0-alpha.1"

    // Generic
    val java = JavaVersion.VERSION_17
    const val MINECRAFT = "1.20.1"
    const val PARCHMENT = "1.20.1:2023.09.03"
    const val JETBRAINS_ANNOTATIONS_VERSION = "24.1.0"
    val SUPPORTED_GAME_VERSIONS: List<String> = listOf(MINECRAFT)

    // Fabric
    const val FABRIC_LOADER = "0.14.21"
    const val FABRIC_API = "0.91.0+1.20.1"

    // Forge
    const val FORGE = "47.1.47"

    // Quilt
    const val QUILT_LOADER = "0.20.2"
    const val QUILT_FABRIC_API = "7.5.0+0.91.0-1.20.1"
}
