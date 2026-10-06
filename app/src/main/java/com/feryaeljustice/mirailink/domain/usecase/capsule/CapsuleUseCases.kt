package com.feryaeljustice.mirailink.domain.usecase.capsule
import com.feryaeljustice.mirailink.domain.repository.CapsuleRepository
import com.feryaeljustice.mirailink.domain.model.capsule.CapsuleAction
class CapsuleUseCases(private val repository: CapsuleRepository) {
    suspend fun cached(peer: String) = repository.cached(peer)
    suspend fun history(peer: String) = repository.history(peer)
    suspend fun act(id: String, action: CapsuleAction) = repository.act(id, action)
    suspend fun send(peer: String, text: String, clientId: String) = repository.send(peer, text, clientId)
}
