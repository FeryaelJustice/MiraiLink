// Feryael Justice
// 2025-11-08

package com.feryaeljustice.mirailink.data.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SessionSerializer : Serializer<Session> {
    override val defaultValue: Session
        get() = Session()

    override suspend fun readFrom(input: InputStream): Session {
        try {
            return Json.decodeFromString(
                Session.serializer(),
                input.readBytes().decodeToString(),
            )
        } catch (e: SerializationException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(
        t: Session,
        output: OutputStream,
    ) {
        output.write(
            Json.encodeToString(Session.serializer(), t).encodeToByteArray(),
        )
    }
}
