package semele.quinn.expandedstorage.plugin.task

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.io.FilterReader
import java.io.Reader
import java.io.StringReader

private val GSON = GsonBuilder().setPrettyPrinting().create()

class JsonNormalizerReader(reader: Reader) : FilterReader(
    StringReader(
        GSON.toJson(
            GSON.fromJson(reader, JsonElement::class.java)
        )
    )
)
