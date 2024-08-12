package dev.compasses.multiloader.extension

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import java.net.URI

abstract class MultiLoaderExtension(val project: Project) {
    private lateinit var _dependencies: List<ModDependency>

    fun dependencies(action: Action<NamedDomainObjectContainer<ModDependency>>) {
        _dependencies = project.container(ModDependency::class.java).run {
            action.execute(this)
            this.forEach(ModDependency::freezeProperties)
            this.filter { it.type.get() != DependencyType.DISABLED }
        }

        val repositories = mutableMapOf<URI, Pair<String, MutableSet<String>>>()

        for (dependency in _dependencies) {
            for (repository in dependency.getRepositories()) {
                val key = project.uri(repository.second)
                if (key in repositories) {
                    repositories[key]!!.second.addAll(repository.third)
                } else {
                    repositories[key] = Pair(repository.first, repository.third.toMutableSet())
                }
            }
        }

        val repositoryHandler = project.repositories

        for (repository in repositories) {
            if (repository.value.second.isEmpty()) {
                repositoryHandler.maven {
                    name = repository.value.first
                    url = repository.key
                }
            } else {
                repositoryHandler.exclusiveContent {
                    forRepositories(repositoryHandler.maven {
                        name = repository.value.first
                        url = repository.key
                    })
                    filter {
                        for (group in repository.value.second) {
                            if (group.endsWith(".*")) {
                                includeGroupAndSubgroups(group.substringBeforeLast(".*"))
                            } else {
                                includeGroup(group)
                            }
                        }
                    }
                }
            }
        }

        val dependencyHandler = project.dependencies
        _dependencies.forEach {
            it.getArtifacts().invoke(dependencyHandler, it.type.get() == DependencyType.REQUIRED || it.enabledAtRuntime.get())
        }
    }

    private fun getDependencies() = if (::_dependencies.isInitialized) {
        _dependencies
    } else {
        listOf()
    }

    fun getDependencyIds(target: UploadTarget, type: DependencyType): Set<String> {
        return getDependencies().filter { it.type.get() == type }.mapNotNull {
            when (target) {
                UploadTarget.CURSEFORGE -> it.curseforgeName.orNull
                UploadTarget.MODRINTH -> it.modrinthName.orNull
            }
        }.toSet()
    }
}
