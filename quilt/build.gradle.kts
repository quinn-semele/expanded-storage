import dev.compasses.multiloader.Constants

plugins {
    id("multiloader-threadlike")
}

multiloader {
    mods {
        create("qsl") {
            required()

            artifacts {
                modImplementation(group = "org.quiltmc", name = "quilt-loader", version = Constants.QUILT_LOADER_VERSION)
                modImplementation(group = "org.quiltmc.quilted-fabric-api", name = "quilted-fabric-api", version = Constants.QUILT_API_VERSION)
            }
        }
    }
}
