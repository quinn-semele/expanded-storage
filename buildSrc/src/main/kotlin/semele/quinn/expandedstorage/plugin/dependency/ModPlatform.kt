package semele.quinn.expandedstorage.plugin.dependency

sealed class ModPlatform(private val prettyName: String) {
    object Common : ModPlatform("common")
    object Fabric : ModPlatform("fabric")
    object Forge : ModPlatform("forge")
    object Thread : ModPlatform("thread")
    object Quilt : ModPlatform("quilt")

    override fun toString(): String {
        return prettyName
    }

    fun isThread(): Boolean {
        return this == Fabric || this == Quilt || this == Thread
    }

    val parent: ModPlatform
        get() = if (this.isThread()) Fabric else this
}
