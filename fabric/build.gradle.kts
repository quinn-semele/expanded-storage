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
        val flkVersion = "1.10.16+kotlin.1.9.21" // https://modrinth.com/mod/fabric-language-kotlin/versions

        implementation("net.fabricmc:fabric-language-kotlin:$flkVersion")
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
        }
    }

    modDependencies.runtimeDependencies(project).forEach {
        modRuntimeOnly(it) {
            exclude(group = "net.fabricmc")
            exclude(group = "net.fabricmc.fabric-api")
        }
    }
}
