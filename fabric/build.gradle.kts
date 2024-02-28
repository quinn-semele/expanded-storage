import semele.quinn.stowage.plugin.Versions

plugins {
    id("dev.architectury.loom")
    id("stowage-generic")
    id("stowage-thread-dependent")
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${Versions.fabricLoader}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.fabricApi}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${Versions.fabricLanguageKotlin}")
}