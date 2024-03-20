package semele.quinn.expandedstorage.plugin.task

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.jvm.tasks.Jar
import java.io.FilterReader
import javax.inject.Inject

open class AbstractJsonTask @Inject constructor(private val reader: Class<FilterReader>) : Jar() {
    @InputFile
    val input: RegularFileProperty = project.objects.fileProperty()

    @Input
    val filePatterns: ListProperty<String> = project.objects.listProperty(String::class.java)
                                                                    .convention(listOf("**/*.json", "**/*.mcmeta"))

    override fun copy() {
        input.finalizeValue()
        filePatterns.finalizeValue()
        this.from(project.zipTree(input.get()))
        this.filesMatching(filePatterns.get()) { filter(reader) }
        super.copy()
    }
}
