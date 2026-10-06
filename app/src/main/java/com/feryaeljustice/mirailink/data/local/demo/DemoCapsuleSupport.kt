package com.feryaeljustice.mirailink.data.local.demo

import com.feryaeljustice.mirailink.data.local.demo.entity.DemoCapsuleEntity
import com.feryaeljustice.mirailink.domain.model.capsule.CrystalCapsule
import com.feryaeljustice.mirailink.domain.model.capsule.PhotoPresentation
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

private val capsuleJson = Json { ignoreUnknownKeys = true }
suspend fun MiraiLinkDemoDatabase.capsulePresentation(peer: String, discoveryCapsule: Boolean = false): PhotoPresentation? {
    val existing = capsuleDao().get(peer)
    if(existing != null) return capsuleJson.decodeFromString<CrystalCapsule>(existing.snapshot).photoPresentation()
    if(matchDao().getMatchByUserId(peer) != null) return null
    return if(discoveryCapsule) PhotoPresentation() else null
}
suspend fun MiraiLinkDemoDatabase.startCapsule(peer: String) {
    val existing = capsuleDao().get(peer)
    if(existing != null) {
        val state = capsuleJson.decodeFromString<CrystalCapsule>(existing.snapshot)
        if(state.status == "cancelled") capsuleDao().save(existing.copy(snapshot = capsuleJson.encodeToString(
            state.copy(status = "paused", revision = state.revision + 1, resumeAccepted = emptyList()))))
        return
    }
    val state = CrystalCapsule(UUID.randomUUID().toString(), listOf(DemoDataSeeder.DEMO_USER_ID, peer))
    // Earlier messages are not capsule contributions.
    val ids = chatDao().getMessagesBetween(DemoDataSeeder.DEMO_USER_ID, peer).map { it.id }
    capsuleDao().save(DemoCapsuleEntity(peer, capsuleJson.encodeToString(state), capsuleJson.encodeToString(ids), null, null, null, "[]"))
}
suspend fun MiraiLinkDemoDatabase.cancelCapsule(peer: String) {
    val existing = capsuleDao().get(peer) ?: return
    val state = capsuleJson.decodeFromString<CrystalCapsule>(existing.snapshot)
    if(state.status != "revealed") capsuleDao().save(existing.copy(snapshot = capsuleJson.encodeToString(
        state.copy(status = "cancelled", revision = state.revision + 1, resumeAccepted = emptyList(), revealRequestedBy = null)), pendingSender = null))
}
