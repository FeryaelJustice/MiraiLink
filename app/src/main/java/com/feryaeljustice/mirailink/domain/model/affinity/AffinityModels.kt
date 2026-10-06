package com.feryaeljustice.mirailink.domain.model.affinity

import kotlinx.serialization.Serializable

@Serializable
data class AffinityPerson(val id: String, val username: String, val nickname: String = "", val avatarUrl: String? = null)
@Serializable
data class AffinityRecommendation(val id: String, val expiresAt: String, val state: String = "available", val commonInterests: List<String> = emptyList(), val person: AffinityPerson? = null)
@Serializable
data class AffinityFeed(val enabled: Boolean = false, val participating: Boolean = true, val eligible: Boolean = false, val items: List<AffinityRecommendation> = emptyList())
@Serializable
data class AffinityLike(val id: String, val person: AffinityPerson? = null, val createdAt: String = "")
@Serializable
data class AffinityLikes(val items: List<AffinityLike> = emptyList())
@Serializable
data class AffinityRequest(val id: String, val incoming: Boolean, val state: String, val text: String, val expiresAt: String, val person: AffinityPerson, val chatId: String? = null)
@Serializable
data class AffinityRequests(val items: List<AffinityRequest> = emptyList())
@Serializable
data class AffinityAction(val id: String = "", val state: String = "", val chatId: String? = null, val match: Boolean = false)
@Serializable
data class AffinityContact(val origin: String = "legacy", val matched: Boolean = false)
@Serializable
data class AffinityMessage(val clientId: String, val text: String)
@Serializable
data class AffinityDemoState(val participating: Boolean = true, val dismissed: Set<String> = emptySet(), val liked: Set<String> = emptySet(), val requests: List<AffinityRequest> = emptyList(), val blocked: Set<String> = emptySet(), val initialized: Boolean = false)
