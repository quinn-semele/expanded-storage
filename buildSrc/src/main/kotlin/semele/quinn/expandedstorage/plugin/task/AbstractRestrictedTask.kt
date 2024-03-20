package semele.quinn.expandedstorage.plugin.task

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import java.io.IOException
import java.nio.file.Files

abstract class AbstractRestrictedTask : DefaultTask() {
    abstract fun doChecks()

    protected fun checkComments(type: String) {
        val comments : MutableList<String> = mutableListOf()

        val regex = ".*// todo-no${type}: (.+)".toRegex()

        project.subprojects.forEach { project: Project ->
            project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.forEach { sourceSet ->
                sourceSet.allJava.forEach { file ->
                    var classPackage : String? = null
                    val className = file.name.substringBefore('.')
                    try {
                        val reader = Files.newBufferedReader(file.toPath())
                        reader.lines().toList().forEachIndexed { index, line ->
                            if (classPackage == null && line.startsWith("package")) {
                                classPackage = line.substring(8 until line.indexOf(';'))
                            } else {
                                val result = regex.matchEntire(line)
                                if (result != null) {
                                    comments.add(" \\- ${result.groups[1]!!.value} @ $classPackage.$className($className.java:${index + 1})")
                                }
                            }

                        }
                    } catch (exception : IOException) {
                        throw RuntimeException("Read failed: ", exception)
                    }
                }
            }
        }

        if (comments.size > 0) {
            val errorMessage = buildString {
                append("Please address the following comments before trying to $type:\n${comments[0]}")
                for (i in 1 until comments.size) {
                    append("\n${comments[i]}")
                }
            }

            throw IllegalStateException(errorMessage)
        }
    }
}
