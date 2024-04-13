import semele.quinn.expandedstorage.plugin.Versions
import semele.quinn.expandedstorage.plugin.dependency.FreezableDependencyList

plugins {
    id("expandedstorage-generic")
    id("expandedstorage-thread-dependencies")
    id("expandedstorage-thread-dependent")
    id("expandedstorage-release-project")
}

loom {
    accessWidenerPath = project(":common").loom.accessWidenerPath
}

val modDependencies = FreezableDependencyList().apply {
    from(project(":thread").extra["mod_dependencies"])

    add("inventory-profiles-next") {
        implementation("org.quiltmc.quilt-kotlin-libraries:quilt-kotlin-libraries:${Versions.QUILT_KOTLIN}")
    }

    freeze()
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
    modImplementation("org.quiltmc.quilted-fabric-api:quilted-fabric-api:${Versions.QUILT_API}")

    modDependencies.compileDependencies(project).forEach {
        modCompileOnly(it) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }

    modDependencies.runtimeDependencies(project).forEach {
        modRuntimeOnly(it) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }
}

