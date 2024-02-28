package semele.quinn.stowage.plugin

import org.gradle.api.JavaVersion

object Versions {
    const val stowage = "1.0.0"
    // Generic
    val java = JavaVersion.VERSION_17
    const val minecraft = "1.20.4"
    const val parchment = "1.20.4:2024.02.25"
    const val kotlinCoroutines = "1.7.3"
    const val kotlinSerialization = "1.6.2"
    // Fabric
    const val fabricLoader = "0.15.7"
    const val fabricApi = "0.96.4+1.20.4"
    const val fabricLanguageKotlin = "1.10.18+kotlin.1.9.22"
    // NeoForge
    const val neoforge = "20.4.188"
    const val kotlinForForge = "4.10.0"
    // Quilt
    const val quiltLoader = "0.23.1"
    const val quiltedFabricApi = ""
    const val quiltKotlinLibraries = ""
}