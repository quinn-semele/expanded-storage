plugins {
    id("java-library")
    id("idea")
    id("net.neoforged.moddev") version "1.0.13"
}

version = "${properties["mod_version"]}+${properties["minecraft_version"]}-neoforge"
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

    accessTransformers {
        from("src/main/resources/META-INF/accesstransformer.cfg")
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
                "--output", file("src/generated/resources/").absolutePath,
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

    create("commonJava") {
        isCanBeResolved = true
    }

    create("commonResources") {
        isCanBeResolved = true
    }
}

repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Unofficial CurseForge Maven"
                url = uri("https://cursemaven.com/")
            }
        }
        filter {
            includeGroup("curse.maven")
        }
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth Maven"
                url = uri("https://api.modrinth.com/maven/")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }

    maven { // Quark, JEI
        name = "Jared"
        url = uri("https://maven.blamejared.com/")
    }

    maven { // Roughly Enough Items
        name = "Shedaniel"
        url = uri("https://maven.shedaniel.me/")
    }

    exclusiveContent { // EMI
        forRepository {
            maven {
                name = "TerraformersMC"
                url = uri("https://maven.terraformersmc.com/")
            }
        }
        filter {
            includeGroup("dev.emi")
        }
    }
}

val enabledMods: List<String> = listOf()

dependencies {
    compileOnly(project(":common"))

    add("commonJava", project(path = ":common", configuration = "commonJava"))
    add("commonResources", project(path = ":common", configuration = "commonResources"))

    compileOnly("dev.emi:emi-neoforge:${properties["emi_version"]}:api")
    compileOnly("mezz.jei:jei-${properties["jei_minecraft_version"]}-neoforge-api:${properties["jei_version"]}")
    compileOnly("me.shedaniel.cloth:cloth-config-neoforge:${properties["cloth_config_version"]}")
    compileOnly("me.shedaniel:RoughlyEnoughItems-api-neoforge:${properties["rei_version"]}")

    if ("emi" in enabledMods) {
        add("localRuntime", "dev.emi:emi-neoforge:${properties["emi_version"]}")
    }

    if ("jei" in enabledMods) {
        add("localRuntime", "mezz.jei:jei-${properties["jei_minecraft_version"]}-neoforge:${properties["jei_version"]}")
    }

    if ("rei" in enabledMods) {
        add("localRuntime", "me.shedaniel:RoughlyEnoughItems-neoforge:${properties["rei_version"]}")
    }

    add(
        if ("carry-on" in enabledMods) "implementation" else "compileOnly",
        "maven.modrinth:carry-on:${properties["carry_on_forge_version"]}"
    )

    add(
        if ("quark" in enabledMods) "implementation" else "compileOnly",
        "org.violetmoon.quark:Quark:${properties["quark_version"]}"
    )

    if ("quark" in enabledMods) {
        add("localRuntime", "org.violetmoon.zeta:Zeta:${properties["zeta_version"]}")
    }
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn(configurations.named("commonJava"))
    source(configurations.named("commonJava"))
}

tasks.withType<ProcessResources> {
    dependsOn(configurations.getByName("commonResources"))
    from(configurations.getByName("commonResources"))

    val replacements = mutableMapOf(
        "minecraft_version" to properties["minecraft_version"] as String,
        "mod_version" to properties["mod_version"] as String,
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
