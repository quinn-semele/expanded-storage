package semele.quinn.expandedstorage.plugin.dependency

import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.DependencyHandlerScope

class DependencyHelper(val scope: DependencyHandlerScope, val providerFactory: ProviderFactory) {
    fun add(configuration: String, dependency: String): Unit {
        add(configuration, dependency) { }
    }

    fun add(configuration: String, dependency: String, configurator: ExternalModuleDependency.() -> Unit): Unit {
        scope.addProvider<String, ExternalModuleDependency>(
                configuration,
                providerFactory.provider { dependency },
        ) {
            this.isTransitive = false
            configurator(this)
        }
    }
}
