package com.feryaeljustice.mirailink.data.datasource

import com.google.firebase.ai.GenerativeModel

class GeminiDataSource(
    private val generativeModel: GenerativeModel,
) {
    /**
     * Envía un prompt aislado al modelo construido por AiModule, sin startChat ni historial.
     * Una respuesta sin text se convierte en cadena vacía; los errores se propagan al use case.
     */
    suspend fun generateContent(prompt: String): String = generativeModel.generateContent(prompt).text ?: ""
}
