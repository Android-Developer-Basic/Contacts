package ru.otus.contacts.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

fun <D: Any> httpResponseModule(dataSerializer: KSerializer<D>) = SerializersModule {
    polymorphic(HttpResponse::class) {
        subclass(HttpResponse.Data.serializer(dataSerializer))
        subclass(HttpResponse.Error.serializer())
    }
}

val httpResponseModule = httpResponseModule(PolymorphicSerializer(Any::class)).plus(SerializersModule {
    polymorphic(Any::class) {
        subclass(SessionClaims::class)
        subclass(ContactsData::class)
    }
})