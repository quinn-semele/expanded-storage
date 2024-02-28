import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("stowage-common-dependent")
}

evaluationDependsOn(":thread")

dependencies {
    compileOnly(project(":thread"))
}

tasks.withType<JavaCompile>().configureEach {
    source(project(":thread").sourceSets.main.get().java)
}

tasks.withType<KotlinCompile>().configureEach {
    source(project(":thread").sourceSets.main.get().kotlin)
}

tasks.withType<ProcessResources>().configureEach {
    from(project(":thread").sourceSets.main.get().resources)
}
