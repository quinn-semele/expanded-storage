import semele.quinn.stowage.plugin.Constants
import semele.quinn.stowage.plugin.Versions

plugins {
    id("dev.architectury.loom")
    id("stowage-generic")
    id("stowage-common-dependent")
}

fabricApi {
    configureDataGeneration {
        modId = Constants.modIdentifier
        outputDirectory = project.file("src/generated/resources/")
    }
}

dependencies {
    modCompileOnly("net.fabricmc:fabric-loader:${Versions.fabricLoader}")
    modLocalRuntime("net.fabricmc:fabric-loader:${Versions.fabricLoader}")

    modCompileOnly("net.fabricmc.fabric-api:fabric-api:${Versions.fabricApi}")
    modLocalRuntime("net.fabricmc.fabric-api:fabric-api:${Versions.fabricApi}")

    modCompileOnly("net.fabricmc:fabric-language-kotlin:${Versions.fabricLanguageKotlin}")
    modLocalRuntime("net.fabricmc:fabric-language-kotlin:${Versions.fabricLanguageKotlin}")
}