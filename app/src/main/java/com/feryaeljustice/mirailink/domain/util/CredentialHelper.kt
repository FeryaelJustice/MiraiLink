package com.feryaeljustice.mirailink.domain.util

import android.content.Context
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential

class CredentialHelper(
    private val context: Context,
) {
    private val credentialManager = CredentialManager.create(context = context)

    suspend fun savePasswordCredential(
        email: String,
        password: String,
    ) {
        val request = CreatePasswordRequest(id = email, password = password)
        try {
            credentialManager.createCredential(context = context, request = request)
        } catch (_: Exception) {
            // El usuario puede cancelar el dialogo o el proveedor puede no estar disponible.
        }
    }

    suspend fun getSavedPasswordCredential(): Pair<String, String>? {
        val request = GetCredentialRequest(listOf(GetPasswordOption()))
        return try {
            val result = credentialManager.getCredential(context = context, request = request)
            val credential = result.credential as? PasswordCredential
            credential?.let { it.id to it.password }
        } catch (_: Exception) {
            null
        }
    }
}
