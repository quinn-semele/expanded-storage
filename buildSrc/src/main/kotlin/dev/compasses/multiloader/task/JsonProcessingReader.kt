package dev.compasses.multiloader.task

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.apache.tools.ant.filters.BaseParamFilterReader
import org.apache.tools.ant.types.Parameterizable
import java.io.Reader
import java.io.StringReader

class JsonProcessingReader(private val reader : Reader) : BaseParamFilterReader(reader), Parameterizable {
    companion object {
        val gson: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    }

    override fun read(): Int {
        if (!initialized) {
            initialize()
        }

        return super.read()
    }

    private var processor: (JsonObject.() -> Unit)? = null

    private fun initialize() {
        val json = gson.fromJson(reader, JsonElement::class.java).asJsonObject

        processor?.invoke(json)

        this.`in` = StringReader(gson.toJson(json))

        initialized = true
    }
}