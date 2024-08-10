package dev.compasses.multiloader.task

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.io.FilterReader
import java.io.Reader
import java.io.StringReader

class JsonNormalizerReader(reader : Reader) : FilterReader(
    StringReader(gson.toJson(gson.fromJson(reader, JsonElement::class.java)))
)  {
    companion object {
        val gson: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    }
}