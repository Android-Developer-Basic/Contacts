package ru.otus.contacts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import ru.otus.contacts.data.ErrorCode.CONFLICT
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class HttpResponseTest {

    @Serializable
    @SerialName("SomeData")
    data class SomeData(val value: String)

    private val json = Json {
        serializersModule = httpResponseModule.plus(SerializersModule {
            polymorphic(Any::class) {
                subclass(SomeData::class)
            }
        })
    }

    @Test
    fun serializesError() {
        val serialized = json.encodeToString(HttpResponse.serializer(SomeData.serializer()), HttpResponse.Error(CONFLICT, "message"))
        val parsed = json.parseToJsonElement (serialized)
        assertTrue { parsed.jsonObject.containsKey("type") }
        assertEquals("Error", parsed.jsonObject["type"]?.jsonPrimitive?.content)
        assertTrue { parsed.jsonObject.containsKey("code") }
        assertEquals("CONFLICT", parsed.jsonObject["code"]?.jsonPrimitive?.content)
        assertTrue { parsed.jsonObject.containsKey("message") }
        assertEquals("message", parsed.jsonObject["message"]?.jsonPrimitive?.content)
    }

    @Test
    fun deserializesError() {
        val deserialized = json.decodeFromString<HttpResponse<SomeData>>("""{"type":"Error","code":"CONFLICT","message":"message"}""")
        assertIs<HttpResponse.Error>(deserialized)
        assertEquals(CONFLICT, deserialized.code)
        assertEquals("message", deserialized.message)
    }

    @Test
    fun serializesData() {
        val serialized = json.encodeToString(
            HttpResponse.serializer(SomeData.serializer()),
            HttpResponse.Data(
                SomeData("data"),
                "message"
            )
        )
        val parsed = json.parseToJsonElement (serialized)
        assertTrue { parsed.jsonObject.containsKey("type") }
        assertEquals("Data", parsed.jsonObject["type"]?.jsonPrimitive?.content)
        assertTrue { parsed.jsonObject.containsKey("data") }
        assertEquals("data", parsed.jsonObject["data"]?.jsonObject?.get("value")?.jsonPrimitive?.content)
        assertTrue { parsed.jsonObject.containsKey("message") }
        assertEquals("message", parsed.jsonObject["message"]?.jsonPrimitive?.content)
    }

    @Test
    fun deserializesData() {
        val deserialized = json.decodeFromString<HttpResponse<SomeData>>(
            """{"type":"Data","data":{"type":"SomeData","value":"data"},"message":"message"}"""
        )
        assertIs<HttpResponse.Data<SomeData>>(deserialized)
        assertEquals<SomeData>(SomeData("data"), deserialized.data)
        assertEquals("message", deserialized.message)
    }
}