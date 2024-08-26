package dev.compasses.multiloader.extension

import org.gradle.api.Named
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import java.net.URI

class ModDependency (private val name: String, objects: ObjectFactory) : Named {
    val type: Property<DependencyType> = objects.property(DependencyType::class).convention(DependencyType.OPTIONAL)
    val curseforgeName: Property<String> = objects.property(String::class).convention(name)
    val modrinthName: Property<String> = objects.property(String::class).convention(name)
    val sourceDirectory: Property<Any> = objects.property(Any::class).convention("src/main/${name.replace("-", "_")}")
    val enabledAtRuntime: Property<Boolean> = objects.property(Boolean::class).convention(false)

    private val repositories: MutableMap<URI, RepositoryExclusions> = mutableMapOf()
    private val artifacts: MutableList<DependencyHandler.(Boolean) -> Unit> = mutableListOf()

    fun required() {
        type.set(DependencyType.REQUIRED)
    }

    fun disabled() {
        type.set(DependencyType.DISABLED)
    }

    fun runtime() {
        enabledAtRuntime.set(true)
    }

    fun requiresRepo(name: String, url: String, groups: Set<String> = setOf()) {
        val uri = URI(url)

        if (uri in repositories) {
            repositories[uri]!!.groups.addAll(groups)
        } else {
            repositories[uri] = RepositoryExclusions(name, groups.toMutableSet())
        }
    }

    fun artifacts(function: DependencyHandler.(enabledAtRuntime: Boolean) -> Unit) {
        artifacts.add(function)
    }

    fun getRepositories() = repositories
    fun getArtifacts() = artifacts

    override fun getName(): String {
        return name
    }
}
