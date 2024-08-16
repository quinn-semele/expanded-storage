package dev.compasses.multiloader.task

import com.google.gson.JsonObject
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.mapProperty

open class ProcessJsonTask : Jar() {
    @InputFile
    val input: RegularFileProperty = project.objects.fileProperty()

    @Input
    val filePatterns: ListProperty<String> = project.objects.listProperty(String::class.java)
        .convention(listOf("**/*.json", "**/*.mcmeta"))

    @Input
    val processors: MapProperty<String, JsonObject.() -> Unit> = project.objects.mapProperty()

    override fun copy() {
        input.finalizeValue()
        filePatterns.finalizeValue()
        processors.finalizeValue()

        val processors = processors.get()

        from(project.zipTree(input.get()))

        filesMatching(filePatterns.get()) {
            filter(
                mapOf("processor" to processors[name]),
                JsonProcessingReader::class.java
            )
        }

        super.copy()
    }
}