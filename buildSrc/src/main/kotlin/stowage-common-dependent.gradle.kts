import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    kotlin("jvm")
}

evaluationDependsOn(":common")

dependencies {
    compileOnly(project(":common"))
}

val notNeoTask = Spec<Task> { task -> !task.name.startsWith("neo") }

tasks.withType<JavaCompile>().matching(notNeoTask).configureEach {
    source(project(":common").sourceSets.main.get().java)
}

tasks.withType<KotlinCompile>().matching(notNeoTask).configureEach {
    source(project(":common").sourceSets.main.get().kotlin)
}

tasks.withType<ProcessResources>().matching(notNeoTask).configureEach {
    from(project(":common").sourceSets.main.get().resources)
}
