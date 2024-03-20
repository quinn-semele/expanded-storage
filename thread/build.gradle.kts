import semele.quinn.expandedstorage.plugin.Constants
import semele.quinn.expandedstorage.plugin.Versions

plugins {
    id("expandedstorage-generic")
    id("expandedstorage-common-dependent")
    id("expandedstorage-dependency-helper")
}

loom {
    accessWidenerPath = project(":common").loom.accessWidenerPath
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")
}

fabricApi.configureDataGeneration {
    outputDirectory = file("src/generated/resources")
    modId = Constants.MOD_ID
}
