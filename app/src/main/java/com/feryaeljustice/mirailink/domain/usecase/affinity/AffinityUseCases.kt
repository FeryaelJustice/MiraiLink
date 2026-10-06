package com.feryaeljustice.mirailink.domain.usecase.affinity

import com.feryaeljustice.mirailink.domain.repository.AffinityRepository

class AffinityUseCases(private val repository: AffinityRepository) {
    suspend fun feed() = repository.feed()
    suspend fun likes(offset: Int = 0) = repository.likes(offset)
    suspend fun requests(offset: Int = 0) = repository.requests(offset)
    suspend fun activity() = repository.activity()
    suspend fun participate(enabled: Boolean) = repository.participate(enabled)
    suspend fun dismiss(id: String) = repository.dismiss(id)
    suspend fun like(id: String) = repository.like(id)
    suspend fun returnLike(id: String) = repository.returnLike(id)
    suspend fun request(id: String, clientId: String, text: String) = repository.request(id, clientId, text)
    suspend fun respond(id: String, accept: Boolean) = repository.respond(id, accept)
    suspend fun block(peerId: String) = repository.block(peerId)
    suspend fun contact(peerId: String) = repository.contact(peerId)
}
