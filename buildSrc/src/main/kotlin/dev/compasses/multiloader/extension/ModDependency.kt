package dev.compasses.multiloader.extension

import org.gradle.api.Named
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Property

abstract class ModDependency : Named {
    private var isFrozen: Boolean = false
    private val repositories = mutableListOf<Triple<String, String, Set<String>>>()
    private lateinit var artifacts: DependencyHandler.(Boolean) -> Unit

    abstract val modrinthName: Property<String>
    abstract val curseforgeName: Property<String>
    abstract val type: Property<DependencyType>
    abstract val enabledAtRuntime: Property<Boolean>

    fun required() {
        type.set(DependencyType.REQUIRED)
    }

    fun disabled() {
        type.set(DependencyType.DISABLED)
    }

    fun runtime() {
        enabledAtRuntime.set(true)
    }

    fun freezeProperties() {
        isFrozen = true

        modrinthName.convention(name).finalizeValue()
        curseforgeName.convention(name).finalizeValue()
        type.convention(DependencyType.OPTIONAL).finalizeValue()
        enabledAtRuntime.convention(false).finalizeValue()
    }

    fun requiresRepo(name: String, url: String, groups: Set<String> = setOf()) {
        if (isFrozen) throw IllegalStateException("Cannot require more repositories when frozen.")
        repositories.add(Triple(name, url, groups))
    }

    fun artifacts(function: DependencyHandler.(enabledAtRuntime: Boolean) -> Unit) {
        if (isFrozen) throw IllegalStateException("Cannot define artifacts when frozen.")
        artifacts = function
    }

    fun getRepositories() = repositories
    fun getArtifacts() = artifacts
}
