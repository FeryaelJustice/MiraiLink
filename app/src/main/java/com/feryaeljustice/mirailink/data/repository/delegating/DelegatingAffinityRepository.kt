package com.feryaeljustice.mirailink.data.repository.delegating

import com.feryaeljustice.mirailink.data.demo.DemoModeManager
import com.feryaeljustice.mirailink.domain.repository.AffinityRepository

class DelegatingAffinityRepository(private val remote: AffinityRepository, private val demo: AffinityRepository, private val mode: DemoModeManager) : AffinityRepository {
    private fun current() = if (mode.isDemoMode.value) demo else remote
    override suspend fun feed() = current().feed()
    override suspend fun likes(offset: Int) = current().likes(offset)
    override suspend fun requests(offset: Int) = current().requests(offset)
    override suspend fun activity() = current().activity()
    override suspend fun participate(enabled: Boolean) = current().participate(enabled)
    override suspend fun dismiss(id: String) = current().dismiss(id)
    override suspend fun like(id: String) = current().like(id)
    override suspend fun returnLike(id: String) = current().returnLike(id)
    override suspend fun request(id: String, clientId: String, text: String) = current().request(id, clientId, text)
    override suspend fun respond(id: String, accept: Boolean) = current().respond(id, accept)
    override suspend fun block(peerId: String) = current().block(peerId)
    override suspend fun contact(peerId: String) = current().contact(peerId)
}
