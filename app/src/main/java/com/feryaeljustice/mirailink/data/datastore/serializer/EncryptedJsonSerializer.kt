package com.feryaeljustice.mirailink.data.datastore.serializer

import androidx.datastore.core.Serializer
import com.feryaeljustice.mirailink.data.datastore.crypto.SecretKeyProvider
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

class EncryptedJsonSerializer<T>(
    private val json: Json,
    private val kSerializer: KSerializer<T>,
    private val default: T,
    private val keyProvider: SecretKeyProvider
) : Serializer<T> {

    override val defaultValue: T = default

    override suspend fun readFrom(input: InputStream): T {
        val all = input.readBytes()
        if (all.isEmpty()) return default
        require(all.size > 12) { "Encrypted payload too short" }

        val iv = all.copyOfRange(0, 12)
        val cipherBytes = all.copyOfRange(12, all.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        // ✅ En DECRYPT sí pasamos el IV
        cipher.init(
            Cipher.DECRYPT_MODE,
            keyProvider.get(),
            GCMParameterSpec(128, iv)
        )
        val clear = cipher.doFinal(cipherBytes)
        val jsonString = String(clear, StandardCharsets.UTF_8)
        return json.decodeFromString(kSerializer, jsonString)
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        val jsonString = json.encodeToString(kSerializer, t)
        val clear = jsonString.toByteArray(StandardCharsets.UTF_8)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        // ✅ En ENCRYPT dejamos que Keystore genere el IV (NO GCMParameterSpec)
        cipher.init(Cipher.ENCRYPT_MODE, keyProvider.get())
        val iv = cipher.iv                       // <- lo da el keystore
        val cipherBytes = cipher.doFinal(clear)

        output.write(iv)                         // prependemos el IV
        output.write(cipherBytes)
        output.flush()
    }
}
