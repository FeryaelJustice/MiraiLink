package com.feryaeljustice.mirailink.core.remoteconfig

import com.feryaeljustice.mirailink.R
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

// Clave para el nombre del modelo en Remote Config
const val GEMINI_MODEL_NAME_KEY = "gemini_model_name"

interface RemoteConfigManager {
    suspend fun initialize()

    fun getGeminiModelName(): String
}

class RemoteConfigManagerImpl : RemoteConfigManager {
    private val remoteConfig: FirebaseRemoteConfig by lazy {
        Firebase.remoteConfig
    }

    override suspend fun initialize() {
        // Configuración para permitir fetches frecuentes en desarrollo
        val configSettings =
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600 // 1 hora en producción, puedes bajarlo para debug
            }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Establece los valores por defecto desde el fichero XML
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        // Intenta obtener y activar los valores más recientes del servidor
        try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            // Si hay un error, la app usará los valores por defecto.
            // Puedes añadir un log aquí para depuración.
            println("Error fetching remote config: ${e.message}")
        }
    }

    override fun getGeminiModelName(): String = remoteConfig.getString(GEMINI_MODEL_NAME_KEY)
}
