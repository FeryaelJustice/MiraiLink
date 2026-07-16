package com.feryaeljustice.mirailink.data.model.response.generic

import kotlinx.serialization.Serializable

/** Minimal allowlisted backend payload used only while classifying remote failures. */
@Serializable
internal data class ApiErrorResponse(
    val code: String? = null,
    val error: String? = null,
    val message: String? = null,
    val chatId: String? = null,
)
