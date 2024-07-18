package semele.quinn.expandedstorage.plugin.dependency

data class DependencyKey(
        val dependencyName: String,
        val cfDependencyName: String? = dependencyName,
        val mrDependencyName: String? = dependencyName,
)
