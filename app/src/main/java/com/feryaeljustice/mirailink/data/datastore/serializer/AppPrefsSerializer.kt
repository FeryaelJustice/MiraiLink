// Feryael Justice
// 2024-07-28

package com.feryaeljustice.mirailink.data.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AppPrefsSerializer : Serializer<AppPrefs> {
    override val defaultValue: AppPrefs
        get() = AppPrefs()

    override suspend fun readFrom(input: InputStream): AppPrefs {
        try {
            return Json.decodeFromString(
                AppPrefs.serializer(), input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: AppPrefs, output: OutputStream) {
        output.write(
            Json.encodeToString(AppPrefs.serializer(), t).encodeToByteArray()
        )
    }
}
