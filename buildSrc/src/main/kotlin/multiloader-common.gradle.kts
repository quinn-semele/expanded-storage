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

afterEvaluate {
    with(sourceSets.main.get()) {
        artifacts {
            java.sourceDirectories.forEach { add("commonJava", it) }
            resources.sourceDirectories.forEach { add("commonResources", it) }
        }
    }
}
