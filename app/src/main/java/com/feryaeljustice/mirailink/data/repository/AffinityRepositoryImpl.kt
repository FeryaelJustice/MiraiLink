package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.remote.AffinityApiService
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.domain.model.affinity.*
import com.feryaeljustice.mirailink.domain.repository.AffinityRepository

class AffinityRepositoryImpl(private val api: AffinityApiService) : AffinityRepository {
    override suspend fun feed() = safeApiCall(NetworkOperation.AUTHENTICATED) { api.feed() }
    override suspend fun likes(offset: Int) = safeApiCall(NetworkOperation.AUTHENTICATED) { api.likes(offset) }
    override suspend fun requests(offset: Int) = safeApiCall(NetworkOperation.AUTHENTICATED) { api.requests(offset) }
    override suspend fun activity() = safeApiCall(NetworkOperation.AUTHENTICATED) { api.activity(); Unit }
    override suspend fun participate(enabled: Boolean) = safeApiCall(NetworkOperation.AUTHENTICATED) { api.participate(mapOf("enabled" to enabled)); Unit }
    override suspend fun dismiss(id: String) = safeApiCall(NetworkOperation.AUTHENTICATED) { api.dismiss(id); Unit }
    override suspend fun like(id: String) = safeApiCall(NetworkOperation.AUTHENTICATED) { api.like(id) }
    override suspend fun returnLike(id: String) = safeApiCall(NetworkOperation.AUTHENTICATED) { api.returnLike(id) }
    override suspend fun request(id: String, clientId: String, text: String) = safeApiCall(NetworkOperation.AUTHENTICATED) { api.request(id, AffinityMessage(clientId, text)) }
    override suspend fun respond(id: String, accept: Boolean) = safeApiCall(NetworkOperation.AUTHENTICATED) { if (accept) api.accept(id) else api.reject(id) }
    override suspend fun block(peerId: String) = safeApiCall(NetworkOperation.AUTHENTICATED) { api.block(peerId); Unit }
    override suspend fun contact(peerId: String) = safeApiCall(NetworkOperation.AUTHENTICATED) { api.contact(peerId) }
}
