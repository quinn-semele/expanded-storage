import semele.quinn.stowage.plugin.Versions

plugins {
    id("dev.architectury.loom")
    id("stowage-generic")
}

dependencies {
    modLocalRuntime("net.fabricmc:fabric-loader:${Versions.fabricLoader}")
}