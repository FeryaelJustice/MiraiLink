package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.affinity.*
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface AffinityRepository {
    suspend fun feed(): MiraiLinkResult<AffinityFeed>
    suspend fun likes(offset: Int = 0): MiraiLinkResult<AffinityLikes>
    suspend fun requests(offset: Int = 0): MiraiLinkResult<AffinityRequests>
    suspend fun activity(): MiraiLinkResult<Unit>
    suspend fun participate(enabled: Boolean): MiraiLinkResult<Unit>
    suspend fun dismiss(id: String): MiraiLinkResult<Unit>
    suspend fun like(id: String): MiraiLinkResult<AffinityAction>
    suspend fun returnLike(id: String): MiraiLinkResult<AffinityAction>
    suspend fun request(id: String, clientId: String, text: String): MiraiLinkResult<AffinityAction>
    suspend fun respond(id: String, accept: Boolean): MiraiLinkResult<AffinityAction>
    suspend fun block(peerId: String): MiraiLinkResult<Unit>
    suspend fun contact(peerId: String): MiraiLinkResult<AffinityContact>
}
