package dev.compasses.multiloader

fun String.toTitleCase(): String {
    return this.split(" ").joinToString(" ") { it.replaceFirstChar(Char::titlecaseChar) }
}