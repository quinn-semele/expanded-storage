package semele.quinn.expandedstorage.plugin.new_dependency

class ModDependency(
        val modName: String,
        val modrinthId: String?,
        val curseforgeId: String?
) {
    val dependencies: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun compileOnly(dependency: String) {
        dependencies.computeIfAbsent("compileOnly") { mutableListOf() }.add(dependency)
    }

    fun runtimeOnly(dependency: String) {
        dependencies.computeIfAbsent("runtimeOnly") { mutableListOf() }.add(dependency)
    }

    fun implementation(dependency: String) {
        dependencies.computeIfAbsent("implementation") { mutableListOf() }.add(dependency)
    }
}
