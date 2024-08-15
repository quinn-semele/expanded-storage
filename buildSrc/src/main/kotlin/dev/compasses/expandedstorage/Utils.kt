package dev.compasses.expandedstorage

import dev.compasses.multiloader.extension.MultiLoaderExtension
import org.gradle.api.Project

fun Project.multiloader(action: MultiLoaderExtension.() -> Unit) {
    this.extensions.configure<MultiLoaderExtension>("multiloader") {
        action(this)
    }
}
