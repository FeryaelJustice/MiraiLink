package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.domain.model.capsule.*
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import retrofit2.http.*

@Serializable data class CapsuleHistoryEnvelope(val messages: List<ChatMessageResponse>, val capsule: CrystalCapsule? = null)
@Serializable(with = CapsuleHistorySerializer::class)
data class CapsuleHistory(val messages: List<ChatMessageResponse>, val capsule: CrystalCapsule? = null)

/** Accepts the legacy array during the server rollout without granting capsule progress. */
object CapsuleHistorySerializer : kotlinx.serialization.KSerializer<CapsuleHistory> {
    private val envelope = object : JsonTransformingSerializer<CapsuleHistoryEnvelope>(CapsuleHistoryEnvelope.serializer()) {
        override fun transformDeserialize(element: JsonElement): JsonElement =
            if (element is JsonArray) JsonObject(mapOf("messages" to element)) else element
    }
    override val descriptor = envelope.descriptor
    override fun deserialize(decoder: kotlinx.serialization.encoding.Decoder): CapsuleHistory =
        envelope.deserialize(decoder).let { CapsuleHistory(it.messages, it.capsule) }
    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: CapsuleHistory) =
        envelope.serialize(encoder, CapsuleHistoryEnvelope(value.messages, value.capsule))
}
@Serializable data class CapsuleActionResponse(val capsule: CrystalCapsule)
@Serializable data class CapsuleConfig(val enabled: Boolean = false, val rulesVersion: Int = 1, val catalogVersion: Int = 1)
@Serializable data class ConfirmedMessage(val id: String = "", val chatId: String = "", val content: String = "", val timestamp: Long = 0)
interface CapsuleApiService {
    @GET("capsules/config") suspend fun config(): CapsuleConfig
    @GET("chats/history/{userId}?include_capsule=true") suspend fun history(@Path("userId") userId: String): CapsuleHistory
    @POST("capsules/{id}/actions") suspend fun action(@Path("id") id: String, @Body action: CapsuleAction): CapsuleActionResponse
    @POST("chats/send") suspend fun send(@Body message: ChatRequest): ConfirmedMessage
}
