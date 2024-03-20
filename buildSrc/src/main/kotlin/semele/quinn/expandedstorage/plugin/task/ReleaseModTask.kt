package semele.quinn.expandedstorage.plugin.task

import java.nio.charset.StandardCharsets

open class ReleaseModTask : AbstractRestrictedTask() {
    override fun doChecks() {
        val gitParentDirectory = project.rootDir

        checkComments("release")

        if (System.getProperty("MOD_IGNORE_CHANGES", "false") != "false") {
            return
        }

        try {
            val process = ProcessBuilder()
                .directory(gitParentDirectory)
                .command("git", "status", "--porcelain")
                .start()

            process.waitFor()

            process.inputReader(StandardCharsets.UTF_8).use { reader ->
                if (reader.readLine() != null) {
                    throw IllegalStateException("Cannot release with uncommitted changes.")
                }
            }
        } catch (error: Exception) {
            throw IllegalStateException("Error occurred whilst checking for uncommitted changes.", error)
        }

        try {
            val process = ProcessBuilder()
                .directory(gitParentDirectory)
                .command("git", "status", "-b", "--porcelain=2")
                .start()

            process.waitFor()

            process.inputReader(StandardCharsets.UTF_8).use { reader ->
                reader.forEachLine { line ->
                    val parts = line.split(" ")
                    if (parts.size >= 2 && "branch.ab" == parts[1]) {
                        if (parts[2] != "+0" || parts[3] != "-0") {
                            throw IllegalStateException("Cannot release with un-pushed changes.")
                        }
                    }
                }
            }
        } catch (error: Exception) {
            throw IllegalStateException("Error occurred whilst checking for un-pushed changes.", error)
        }
    }
}
