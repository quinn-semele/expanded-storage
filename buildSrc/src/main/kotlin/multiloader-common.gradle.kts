import dev.compasses.multiloader.Constants

plugins {
    id("multiloader-shared")
    id("net.neoforged.moddev")
}

neoForge {
    neoFormVersion = Constants.NEOFORM_VERSION

    parchment {
        minecraftVersion = Constants.PARCHMENT_MINECRAFT
        mappingsVersion = Constants.PARCHMENT_RELEASE
    }
}

dependencies {
    compileOnly(group = "org.spongepowered", name = "mixin", version = Constants.MIXIN_VERSION)
    annotationProcessor(compileOnly(group = "io.github.llamalad7", name = "mixinextras-common", version = Constants.MIXIN_EXTRAS_VERSION))
}

configurations {
    create("commonJava") { isCanBeResolved = false; isCanBeConsumed = true }
    create("commonResources") { isCanBeResolved = false; isCanBeConsumed = true }
}

artifacts {
    val mainSourceSet = sourceSets.main.get()

    for (sourceDirectory in mainSourceSet.java.sourceDirectories) {
        add("commonJava", sourceDirectory)
    }

    for (sourceDirectory in mainSourceSet.resources.sourceDirectories) {
        add("commonResources", sourceDirectory)
    }
}
