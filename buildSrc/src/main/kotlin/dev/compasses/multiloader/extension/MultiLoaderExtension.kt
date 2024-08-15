package dev.compasses.multiloader.extension

import org.gradle.api.NamedDomainObjectContainer

interface MultiLoaderExtension {
    val mods: NamedDomainObjectContainer<ModDependency>

    fun getDependencyIds(target: UploadTarget, type: DependencyType): Set<String> {
        return mods.filter { it.type.get() == type }.mapNotNull {
            when (target) {
                UploadTarget.CURSEFORGE -> it.curseforgeName.orNull
                UploadTarget.MODRINTH -> it.modrinthName.orNull
            }
        }.toSet()
    }
}
