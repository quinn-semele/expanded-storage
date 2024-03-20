import semele.quinn.expandedstorage.plugin.Versions

plugins {
    id("expandedstorage-generic")
    id("expandedstorage-thread-dependent")
    id("expandedstorage-release-project")
    id("expandedstorage-dependency-helper")
}

loom {
    accessWidenerPath = project(":common").loom.accessWidenerPath
}

repositories {
    maven {
        name = "Quilt Release Maven"
        url = uri("https://maven.quiltmc.org/repository/release")

        content {
            includeGroupByRegex("org\\.quiltmc(\\.[a-z_-]+)*")
        }
    }

    maven {
        name = "Quilt Snapshot Maven"
        url = uri("https://maven.quiltmc.org/repository/snapshot")

        content {
            includeGroupByRegex("org\\.quiltmc(\\.[a-z_-]+)*")
        }
    }
}

dependencies {
    modImplementation("org.quiltmc:quilt-loader:${Versions.QUILT_LOADER}")
    modImplementation("org.quiltmc.quilted-fabric-api:quilted-fabric-api:${Versions.QUILT_FABRIC_API}")
}

