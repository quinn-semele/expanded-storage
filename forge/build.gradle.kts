plugins {
    id("java-library")
    id("idea")
    id("net.neoforged.moddev") version "1.0.10"
}

//tasks.named('wrapper', Wrapper).configure {
//    distributionType = Wrapper.DistributionType.BIN
//}

version = properties["mod_version"] as String
group = properties["mod_group"] as String
val mod_id: String by properties

base.archivesName.set(mod_id)

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = properties["neoforge_version"] as String

    parchment {
        mappingsVersion = properties["parchment_version"] as String
        mappingsVersion = properties["minecraft_version"] as String
    }

    runs {
        create("client") {
            client()
        }

        create("server") {
            server()
            programArgument("--nogui")
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("data") {
            data()
            programArguments.addAll(listOf(
                "--mod", mod_id,
                "--all",
                "--output", file("src/generated/resources/").absolutePath
                "--existing", file("src/main/resources/").absolutePath
            ))
        }

        configureEach {
            logLevel = org.slf4j.event.Level.INFO
        }
    }

    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.configure {
    resources.srcDir("src/generated/resources/")
}

configurations {
    val localRuntime = create("localRuntime")
    runtimeClasspath.configure { extendsFrom(localRuntime) }
}

tasks.withType<ProcessResources> {
    val replacements = mutableMapOf(
        "minecraft_version" to properties["minecraft_version"] as String,
    )

    inputs.properties(replacements)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replacements)
    }
}

//tasks.withType(ProcessResources).configureEach {
//    var replaceProperties = [
//        minecraft_version      : minecraft_version,
//    minecraft_version_range: minecraft_version_range,
//    neo_version            : neo_version,
//    neo_version_range      : neo_version_range,
//    loader_version_range   : loader_version_range,
//    mod_id                 : mod_id,
//    mod_name               : mod_name,
//    mod_license            : mod_license,
//    mod_version            : mod_version,
//    mod_authors            : mod_authors,
//    mod_description        : mod_description
//    ]

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
