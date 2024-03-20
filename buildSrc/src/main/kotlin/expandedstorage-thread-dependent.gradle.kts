plugins {
    id("expandedstorage-common-dependent")
}

evaluationDependsOn(":thread")

dependencies {
    compileOnly(project(":thread"))
}

val notNeoTask = Spec<Task> { task -> !task.name.startsWith("neo") }

tasks.withType<JavaCompile>().matching(notNeoTask).configureEach {
    source(project(":thread").sourceSets.main.get().java)
}

tasks.withType<ProcessResources>().matching(notNeoTask).configureEach {
    from(project(":thread").sourceSets.main.get().resources) {
        exclude("fabric.mod.json")
    }
}
