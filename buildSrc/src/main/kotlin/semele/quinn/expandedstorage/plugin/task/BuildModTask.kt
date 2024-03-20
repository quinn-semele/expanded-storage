package semele.quinn.expandedstorage.plugin.task

open class BuildModTask : AbstractRestrictedTask() {
    override fun doChecks() {
        checkComments("build")
    }
}
