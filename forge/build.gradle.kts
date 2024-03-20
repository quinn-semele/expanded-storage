import semele.quinn.expandedstorage.plugin.Constants
import semele.quinn.expandedstorage.plugin.Versions

plugins {
    id("expandedstorage-generic")
    id("expandedstorage-common-dependent")
    id("expandedstorage-release-project")
    id("expandedstorage-dependency-helper")
}

loom {
    accessWidenerPath = project(":common").loom.accessWidenerPath

    forge {
        mixinConfig("expandedstorage-common.mixins.json")
        mixinConfig("expandedstorage-forge.mixins.json")

        convertAccessWideners = true
    }

    runs {
        create("datagen") {
            data()

            programArg("--existing")
            programArg(file("src/main/resources").absolutePath)

            programArg("--existing")
            programArg(project(":common").file("src/main/resources").absolutePath)


            programArg("--all")

            programArg("--mod")
            programArg(Constants.MOD_ID)

            programArg("--output")
            programArg(file("src/generated/resources").absolutePath)
        }
    }
}

sourceSets.main {
    resources.srcDir(file("src/generated/resources"))
}

tasks.getByName<Jar>("jar") {
    manifest.attributes(mapOf(
            "Automatic-Module-Name" to "ellemes.expandedstorage",
            "MixinConfigs" to loom.forge.mixinConfigs.get().joinToString(",")
    ))
}

dependencies {
    forge("net.minecraftforge:forge:${Versions.MINECRAFT}-${Versions.FORGE}")
}
