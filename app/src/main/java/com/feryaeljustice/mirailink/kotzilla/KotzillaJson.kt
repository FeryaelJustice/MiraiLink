@file:Suppress("SerialVersionUIDInSerializableClass")

package com.feryaeljustice.mirailink.kotzilla

/*import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class KotzillaJson(
    val sdkVersion: String,
    val keys: List<KotzillaKey>,
)

@Serializable
data class KotzillaKey(
    val appId: String,
    val applicationPackageName: String,
    val keyId: String,
    val apiKey: String,
)

object KotzillaConfigLoader {
    private val json = Json { ignoreUnknownKeys = true }

    fun loadForThisApp(context: Context): KotzillaKey? {
        val pkg = context.packageName
        val bytes = context.assets.open("kotzilla.json").use { it.readBytes() }
        val cfg = json.decodeFromString<KotzillaJson>(String(bytes))
        return cfg.keys.firstOrNull { it.applicationPackageName == pkg }
    }
}*/
