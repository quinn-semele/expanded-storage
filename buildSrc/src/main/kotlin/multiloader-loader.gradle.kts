plugins {
    id("multiloader-shared")
}

configurations {
    create("commonJava") { isCanBeResolved = true }
    create("commonResources") { isCanBeResolved = true }
}

dependencies {
    compileOnly(project(":common"))

    "commonJava"(project(path=":common", configuration="commonJava"))
    "commonResources"(project(path=":common", configuration="commonResources"))
}

tasks {
    "compileJava"(JavaCompile::class) {
        dependsOn(configurations.getByName("commonJava"))
        source(configurations.getByName("commonJava"))
    }

    processResources {
        dependsOn(configurations.getByName("commonResources"))
        from(configurations.getByName("commonResources"))
    }
}