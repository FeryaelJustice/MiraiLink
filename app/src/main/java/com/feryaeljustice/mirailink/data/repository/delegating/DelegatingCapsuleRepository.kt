package com.feryaeljustice.mirailink.data.repository.delegating
import com.feryaeljustice.mirailink.data.demo.DemoModeManager
import com.feryaeljustice.mirailink.domain.repository.CapsuleRepository
import com.feryaeljustice.mirailink.domain.model.capsule.CapsuleAction
class DelegatingCapsuleRepository(private val remote: CapsuleRepository, private val demo: CapsuleRepository,
    private val mode: DemoModeManager) : CapsuleRepository {
    private fun current() = if(mode.isDemoMode.value) demo else remote
    override suspend fun available() = current().available()
    override suspend fun cached(peerId: String) = current().cached(peerId)
    override suspend fun history(peerId: String) = current().history(peerId)
    override suspend fun act(id: String, action: CapsuleAction) = current().act(id, action)
    override suspend fun send(peerId: String, text: String, clientId: String) = current().send(peerId, text, clientId)
}
