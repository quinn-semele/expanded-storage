import semele.quinn.expandedstorage.plugin.Constants
import semele.quinn.expandedstorage.plugin.task.AbstractJsonTask
import semele.quinn.expandedstorage.plugin.task.JsonNormalizerReader

plugins {
    id("java-library")
    id("dev.architectury.loom")
}

tasks {
    jar {
        archiveClassifier = "dev"

        from(rootProject.file("LICENSE"))
    }

    remapJar {
        archiveClassifier = "fat"
    }

    val minJarTask = register("minJar", AbstractJsonTask::class.java, JsonNormalizerReader::class.java)

    minJarTask.configure {
        input.set(remapJar.get().outputs.files.singleFile)
        archiveClassifier.set(project.name)

        manifest {
            from(zipTree(remapJar.get().outputs.files.first()).single { it.name == "MANIFEST.MF" })
        }

        dependsOn(remapJar)
    }

    build {
        dependsOn(minJarTask)
    }

}

rootProject.tasks.getByName(Constants.BUILD_MOD_TASK).dependsOn(tasks.build)
