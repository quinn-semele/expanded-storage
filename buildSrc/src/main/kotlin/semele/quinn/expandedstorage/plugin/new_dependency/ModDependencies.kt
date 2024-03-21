package semele.quinn.expandedstorage.plugin.new_dependency

class ModDependencies {
    private val dependencies = mutableListOf<ModDependency>()
    private val enabledMods = mutableListOf<String>()

    fun add(
        modName: String,
        modrinthDependencyName: String? = modName,
        curseforgeDependencyName: String? = modName,
        configurationAction: ModDependency.() -> Unit
    ) {
        dependencies.add(ModDependency(modName, modrinthDependencyName, curseforgeDependencyName).apply(configurationAction))
    }

    fun enableMods(vararg modNames: String) {
        enabledMods.addAll(modNames)
    }

    fun iterateCompileDependencies(action: (String) -> Unit) {
        dependencies.map { dependency ->
            val compileOnly = dependency.dependencies["compileOnly"] ?: mutableListOf()
            val implementation = dependency.dependencies["implementation"] ?: mutableListOf()

            compileOnly + implementation
        }.flatten().distinct().forEach(action)
    }

    fun iterateRuntimeDependencies(action: (String) -> Unit) {
        dependencies.filter { it.modName in enabledMods }
            .map { dependency ->
                val runtimeOnly = dependency.dependencies["runtimeOnly"] ?: mutableListOf()
                val implementation = dependency.dependencies["implementation"] ?: mutableListOf()

                runtimeOnly + implementation
            }.flatten().distinct().forEach(action)
    }

    fun getModrinthIds() = dependencies.mapNotNull { it.modrinthId }
    fun getCurseforgeIds() = dependencies.mapNotNull { it.curseforgeId }
}


