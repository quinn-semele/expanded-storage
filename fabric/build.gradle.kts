import semele.quinn.expandedstorage.plugin.Versions

plugins {
    id("expandedstorage-generic")
    id("expandedstorage-thread-dependencies")
    id("expandedstorage-thread-dependent")
    id("expandedstorage-release-project")
}

loom {
    accessWidenerPath = project(":common").loom.accessWidenerPath
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")
}
