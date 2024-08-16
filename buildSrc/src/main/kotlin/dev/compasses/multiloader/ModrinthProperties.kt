package dev.compasses.multiloader

abstract class ModrinthProperties {
    abstract val projectId: String
    open val uploadToken: String = "MODRINTH_TOKEN"
}