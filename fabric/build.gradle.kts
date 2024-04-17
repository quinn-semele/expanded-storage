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
        implementation("net.fabricmc:fabric-language-kotlin:${Versions.FABRIC_KOTLIN}")
    }

    freeze()
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")

    modDependencies.compileDependencies(project).forEach {
        modCompileOnly(it) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
            exclude(group = "com.github.astei")
        }
    }

    modDependencies.runtimeDependencies(project).forEach {
        modRuntimeOnly(it) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
            exclude(group = "com.github.astei")
        }
    }
}
