package semele.quinn.expandedstorage.plugin.dependency

class DependencyMapProxy(val put: (String, String) -> Unit) {
    fun compileOnly(dependencyNotation: String) {
        put("compileOnly", dependencyNotation)
    }

    fun runtimeOnly(dependencyNotation: String) {
        put("runtimeOnly", dependencyNotation)
    }

    fun implementation(dependencyNotation: String) {
        put("implementation", dependencyNotation)
    }
}
