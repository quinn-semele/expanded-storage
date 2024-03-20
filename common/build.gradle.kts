import semele.quinn.expandedstorage.plugin.Versions

plugins {
    id("expandedstorage-generic")
    id("expandedstorage-dependency-helper")
}

loom {
    accessWidenerPath = file("src/main/resources/expandedstorage.accessWidener")
}

dependencies {
    modRuntimeOnly(modCompileOnly("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")!!)
}
