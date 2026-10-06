package com.feryaeljustice.mirailink.domain.repository
import com.feryaeljustice.mirailink.domain.model.capsule.*
import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
data class CapsuleConversation(val messages: List<ChatMessage>, val capsule: CrystalCapsule?)
interface CapsuleRepository {
    suspend fun available(): Boolean
    suspend fun cached(peerId: String): CrystalCapsule?
    suspend fun history(peerId: String): MiraiLinkResult<CapsuleConversation>
    suspend fun act(id: String, action: CapsuleAction): MiraiLinkResult<CrystalCapsule>
    suspend fun send(peerId: String, text: String, clientId: String): MiraiLinkResult<Unit>
}
