package dev.compasses.multiloader

import dev.compasses.multiloader.extension.MultiLoaderExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName

fun Project.multiloader() = this.extensions.getByName<MultiLoaderExtension>("multiloader")

fun Project.multiloader(action: Action<MultiLoaderExtension>) = this.extensions.configure("multiloader", action)

fun String.toTitleCase(): String {
    return this.split(" ").joinToString(" ") { it.replaceFirstChar(Char::titlecaseChar) }
}
