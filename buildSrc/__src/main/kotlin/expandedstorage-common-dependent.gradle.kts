plugins {
    id("java-library")
}

evaluationDependsOn(":common")

dependencies {
    compileOnly(project(":common"))
}

val notNeoTask = Spec<Task> { task -> !task.name.startsWith("neo") }

tasks.withType<JavaCompile>().matching(notNeoTask).configureEach {
    source(project(":common").sourceSets.main.get().java)
}

tasks.withType<ProcessResources>().matching(notNeoTask).configureEach {
    from(project(":common").sourceSets.main.get().resources)
}
