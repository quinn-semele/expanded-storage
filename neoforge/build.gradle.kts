import dev.compasses.multiloader.Constants

plugins {
    id("multiloader-neoforge")
}

neoForge {
    runs {
        create("commonData") {
            data()

            programArguments.addAll(
                "--mod", Constants.MOD_ID,
                "--output", findProject(":common")!!.file("src/generated/resources").absolutePath,
                "--existing", findProject(":common")!!.file("src/main/resources").absolutePath,
                "--client"
            )

            systemProperty("expandedstorage.datagen.common", "true")
        }

        named("data") {
            programArguments.addAll(
                "--existing", findProject(":common")!!.file("src/generated/resources").absolutePath
            )
        }
    }
}