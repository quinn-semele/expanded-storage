package dev.compasses.multiloader

import org.gradle.jvm.toolchain.JavaLanguageVersion

object Constants {
    const val GROUP = "dev.compasses.expandedstorage"
    const val MOD_ID = "expandedstorage"
    const val MOD_NAME = "Expanded Storage"
    const val MOD_VERSION = "14.1.3"
    const val LICENSE = "Multiple"
    const val DESCRIPTION = "A mod adding various storage types, including double iron chests."

    const val HOMEPAGE = "https://www.curseforge.com/minecraft/mc-mods/expanded-storage"
    const val ISSUE_TRACKER = "https://github.com/quinn-semele/expanded-storage/issues"
    const val SOURCES_URL = "https://github.com/quinn-semele/expanded-storage"

    @Suppress("RedundantNullableReturnType")
    val curseforgeProperties: CurseForgeProperties? = object : CurseForgeProperties() {
        override val projectId = "978068"
        override val projectSlug = "expanded-storage"

    }

    @Suppress("RedundantNullableReturnType")
    val modrinthProperties: ModrinthProperties? = object : ModrinthProperties() {
        override val projectId: String = "jCCPlP3c"
    }

    const val PUBLISH_WEBHOOK_VARIABLE = "PUBLISH_WEBHOOK"

    const val COMPARE_URL = "https://github.com/quinn-semele/expanded-storage/compare/"

    val CONTRIBUTORS = linkedMapOf(
        "Quinn Semele" to "Project Owner"
    )

    val CREDITS = linkedMapOf<String, String>(

    )

    val EXTRA_MOD_INFO_REPLACEMENTS = mapOf<String, String>(

    )

    val JAVA_VERSION = JavaLanguageVersion.of(21)
    const val JETBRAIN_ANNOTATIONS_VERSION = "24.1.0"
    const val FINDBUGS_VERSION = "3.0.2"

    const val MIXIN_VERSION = "0.8.5"
    const val MIXIN_EXTRAS_VERSION = "0.3.5"

    const val MINECRAFT_VERSION = "1.21"
    const val FL_MINECRAFT_CONSTRAINT = ">=1.21- <1.22"
    const val NF_MINECRAFT_CONSTRAINT = "[1.21, 1.22)"
    val SUPPORTED_MINECRAFT_VERSIONS = listOf(MINECRAFT_VERSION, "1.21.1")

    // https://parchmentmc.org/docs/getting-started#choose-a-version/
    const val PARCHMENT_MINECRAFT = "1.21"
    const val PARCHMENT_RELEASE = "2024.07.28"

    // https://fabricmc.net/develop/
    const val FABRIC_API_VERSION = "0.102.0+1.21"
    const val FABRIC_LOADER_VERSION = "0.15.11"

    // https://quiltmc.org/en/usage/latest-versions/
    const val QUILT_API_VERSION = "11.0.0-alpha.3+0.100.7-1.21"
    const val QUILT_LOADER_VERSION = "0.26.3"

    const val NEOFORM_VERSION = "1.21-20240613.152323" // // https://projects.neoforged.net/neoforged/neoform/
    const val NEOFORGE_VERSION = "21.0.167" // https://projects.neoforged.net/neoforged/neoforge/
    const val FML_CONSTRAINT = "[4,)" // https://projects.neoforged.net/neoforged/fancymodloader/
}
