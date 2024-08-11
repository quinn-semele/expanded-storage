import dev.compasses.multiloader.Constants
import dev.compasses.multiloader.extension.DependencyType

plugins {
    id("multiloader-threadlike")
}

multiloader {
    dependencies {
        create("qsl") {
            type = DependencyType.REQUIRED

            requiresRepo("QuiltMC Maven", "https://maven.quiltmc.org/repository/release/", setOf(
                "org.quiltmc",
                "org.quiltmc.quilted-fabric-api"
            ))

            artifacts {
                modImplementation(group = "net.fabricmc", name = "fabric-loader", version = Constants.FABRIC_LOADER_VERSION)
                modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = Constants.FABRIC_API_VERSION)
            }
        }
    }
}
