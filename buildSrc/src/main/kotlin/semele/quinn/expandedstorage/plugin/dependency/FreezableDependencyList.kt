package semele.quinn.expandedstorage.plugin.dependency

import org.gradle.api.Project

// todo: better name
typealias DependencyAdder = DependencyMapProxy.(Project) -> Unit

class FreezableDependencyList {
    private var frozen: Boolean = false
    private val builders: MutableMap<DependencyKey, Dependency> = mutableMapOf()
    private val enabledMods: MutableList<String> = mutableListOf()
    private val dependencies: MutableMap<String, MutableMap<String, MutableList<String>>> = mutableMapOf()

    fun freeze() {
        this.frozen = true
    }

    fun enableMods(vararg mods: String) {
        enabledMods.addAll(mods)
    }

    fun from(other: Any?) {
        if (other !is FreezableDependencyList) {
            throw IllegalStateException("other must be a FreezableDependencyList.")
        }

        if (frozen) {
            throw IllegalStateException("Cannot add to a frozen dependency list.")
        }

        if (!other.frozen) {
            throw IllegalStateException("Can only add from a frozen dependency list.")
        }

        other.builders.forEach { (key, dependency) ->
            dependency.adders.forEach { adder ->
                add(key, adder)
            }
        }
    }

    fun add(dependencyName: String,
            cfDependencyName: String? = dependencyName,
            mrDependencyName: String? = dependencyName,
            action: DependencyAdder
    ) {
        val key = DependencyKey(dependencyName, cfDependencyName, mrDependencyName)

        add(key, action)
    }

    fun compileDependencies(project: Project): List<String> {
        collectDependencies(project)

        return dependencies.map {
            it.value.getOrDefault("compileOnly", listOf()) + it.value.getOrDefault("implementation", listOf())
        }.flatten()
    }

    fun runtimeDependencies(project: Project): List<String> {
        collectDependencies(project)

        return dependencies.mapNotNull {
            if (it.key in enabledMods) {
                it.value.getOrDefault("runtimeOnly", listOf()) + it.value.getOrDefault("implementation", listOf())
            } else {
                null
            }
        }.flatten()
    }

    // Internal methods
    private fun add(key: DependencyKey, action: DependencyAdder) {
        if (frozen) {
            throw IllegalStateException("Cannot add any more dependencies now this list is frozen.")
        }

        val existing = builders[key]

        if (existing != null) {
            existing.adders += action

            return
        }

        builders[key] = Dependency(mutableListOf(action))
    }

    private fun collectDependencies(project: Project) {
        if (!frozen) {
            throw IllegalStateException("Please freeze this list first.")
        }

        if (dependencies.isEmpty()) {
            for ((key, dependency) in builders) {
                val map = dependencies.computeIfAbsent(key.dependencyName) {
                    mutableMapOf()
                }
                val mapProxy = DependencyMapProxy { scope, notation ->
                    map.computeIfAbsent(scope) { mutableListOf() }.add(notation)
                }

                dependency.adders.forEach { adder ->
                    adder(mapProxy, project)
                }
            }
        }
    }

    fun curseForgeIds(): List<String> = builders.keys.mapNotNull { it.cfDependencyName }
    fun modrinthIds(): List<String> = builders.keys.mapNotNull { it.mrDependencyName }
}
