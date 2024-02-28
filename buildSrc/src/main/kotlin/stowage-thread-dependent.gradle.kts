plugins {
    id("java-library")
    id("stowage-common-dependent")
}

evaluationDependsOn(":thread")

dependencies {
    compileOnly(project(":thread"))
}

tasks.withType<JavaCompile>().configureEach {
    source(project(":thread").sourceSets.main.get().java)
}

tasks.withType<ProcessResources>().configureEach {
    from(project(":thread").sourceSets.main.get().resources)
}
