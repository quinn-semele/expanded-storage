import semele.quinn.stowage.plugin.Versions

plugins {
    id("dev.architectury.loom")
    id("stowage-generic")
    id("stowage-thread-dependent")
}

repositories {
    maven {
        name = "Quilt Release Maven"
        url = uri("https://maven.quiltmc.org/repository/release/")
    }
}

dependencies {
    modImplementation("org.quiltmc:quilt-loader:${Versions.quiltLoader}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.fabricApi}") // todo: replace with QFAPI when released.
}