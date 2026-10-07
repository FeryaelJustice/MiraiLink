package com.feryaeljustice.mirailink.ui.screens.affinity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.model.affinity.*
import com.feryaeljustice.mirailink.domain.usecase.affinity.AffinityUseCases
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import java.util.UUID

data class AffinityUiState(val loading: Boolean = false, val busy: Boolean = false, val feed: AffinityFeed = AffinityFeed(), val likes: List<AffinityLike> = emptyList(), val requests: List<AffinityRequest> = emptyList(), val error: UiError? = null, val draft: String = "", val selected: AffinityRecommendation? = null, val canLoadMore: Boolean = false, val reportPeer: String? = null, val reportReason: String = "", val isDemo: Boolean = false, val isPlus: Boolean = false)

class AffinityViewModel(private val cases: AffinityUseCases, private val session: GlobalMiraiLinkSession, private val saved: SavedStateHandle, private val reportUseCase: com.feryaeljustice.mirailink.domain.usecase.report.ReportUseCase) : ViewModel() {
    private val mutable = MutableStateFlow(AffinityUiState(draft = saved["affinityDraft"] ?: ""))
    val state = mutable.asStateFlow()
    private var owner: String? = null
    private var requestOffset = 0
    private val hiddenPeers = (saved.get<List<String>>("affinityHiddenPeers") ?: emptyList()).toMutableSet()
    private fun visibleRequests(rows: List<AffinityRequest>) = rows.filter {
        it.state == "pending" && it.person.id !in hiddenPeers && runCatching { java.time.Instant.parse(it.expiresAt).isAfter(java.time.Instant.now()) }.getOrDefault(false)
    }
    private var refreshJob: Job? = null
    private var mutationJob: Job? = null
    init {
        viewModelScope.launch {
            combine(session.isPlus, session.isDemoMode, session.currentUserId) { plus, demo, user -> Triple(plus, demo, user) }.collect { (plus, demo, user) ->
                val key = "$demo:$user"
                // Await the persisted identity before restoring its draft on process recreation.
                if (owner == null && user == null) return@collect
                if ((owner ?: saved.get<String>("affinityOwner")) != key) {
                    refreshJob?.cancel(); mutationJob?.cancel(); hiddenPeers.clear(); saved.remove<List<String>>("affinityHiddenPeers"); requestOffset = 0
                    saved.remove<String>("affinityDraft"); saved.remove<String>("affinityClientId"); saved.remove<String>("affinitySelected"); mutable.value = AffinityUiState(isDemo = demo, isPlus = plus)
                } else {
                    mutable.update { it.copy(isDemo = demo, isPlus = plus) }
                }
                owner = key; saved["affinityOwner"] = key
                if (user != null) refresh()
            }
        }
    }
    fun resetDemo() = action { cases.resetDemo() }
    fun refresh() {
        if (mutable.value.busy) return
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch { load() }
    }
    private suspend fun load() {
        val captured = owner
        mutable.update { it.copy(loading = true, error = null) }
        val feed = cases.feed(); val likes = cases.likes(); val requests = cases.requests()
        if (captured != owner) return
        requestOffset = (requests as? MiraiLinkResult.Success)?.data?.items?.size ?: requestOffset
        mutable.update { current -> current.copy(loading = false,
            feed = (feed as? MiraiLinkResult.Success)?.data ?: current.feed,
            likes = (likes as? MiraiLinkResult.Success)?.data?.items ?: current.likes,
            requests = (requests as? MiraiLinkResult.Success)?.data?.items?.let(::visibleRequests) ?: current.requests,
            canLoadMore = (requests as? MiraiLinkResult.Success)?.data?.items?.size == 20,
            error = listOf(feed, likes, requests).filterIsInstance<MiraiLinkResult.Error>().firstOrNull()?.error?.toUiError(),
        ) }
        val id: String? = saved["affinitySelected"]
        if (id != null) mutable.update { it.copy(selected = it.feed.items.find { row -> row.id == id }) }
    }
    fun moreRequests() = mutate {
        when (val result = cases.requests(requestOffset)) {
            is MiraiLinkResult.Error -> result
            is MiraiLinkResult.Success -> { requestOffset += result.data.items.size; mutable.update { it.copy(requests = (it.requests + visibleRequests(result.data.items)).distinctBy { r -> r.id }, canLoadMore = result.data.items.size == 20) }; MiraiLinkResult.Success(Unit) }
        }
    }
    fun editDraft(text: String) { saved["affinityDraft"] = text; mutable.update { it.copy(draft = text) } }
    fun select(row: AffinityRecommendation?) {
        if (row?.id != mutable.value.selected?.id) { saved["affinityClientId"] = UUID.randomUUID().toString(); editDraft("") }
        saved["affinitySelected"] = row?.id; mutable.update { it.copy(selected = row) }
    }
    fun send() {
        val row = mutable.value.selected ?: return
        val text = mutable.value.draft.trim(); if (text.isBlank()) return
        val id = saved.get<String>("affinityClientId") ?: UUID.randomUUID().toString().also { saved["affinityClientId"] = it }
        mutate { when (val result = cases.request(row.id, id, text)) {
            is MiraiLinkResult.Error -> result
            is MiraiLinkResult.Success -> { select(null); editDraft(""); load(); MiraiLinkResult.Success(Unit) }
        } }
    }
    fun like(id: String) = action { cases.like(id) }
    fun returnLike(id: String) = action { cases.returnLike(id) }
    fun dismiss(id: String) = action { cases.dismiss(id) }
    fun participate(enabled: Boolean) = action { cases.participate(enabled) }
    fun respond(id: String, accept: Boolean) = action {
        val result = cases.respond(id, accept)
        if (result is MiraiLinkResult.Success) mutable.update { it.copy(requests = it.requests.filterNot { r -> r.id == id }) }
        result
    }
    fun block(peerId: String) = action {
        val result = cases.block(peerId)
        if (result is MiraiLinkResult.Success) mutable.update { it.copy(requests = it.requests.filterNot { r -> r.person.id == peerId }) }
        result
    }
    fun openReport(peerId: String) { mutable.update { it.copy(reportPeer = peerId, reportReason = "") } }
    fun closeReport() { mutable.update { it.copy(reportPeer = null) } }
    fun editReport(reason: String) { mutable.update { it.copy(reportReason = reason) } }
    fun report() {
        val peer = mutable.value.reportPeer ?: return
        val reason = mutable.value.reportReason.trim()
        if (reason.isBlank()) return
        action { val result = reportUseCase(peer, reason); if (result is MiraiLinkResult.Success) {
            hiddenPeers.add(peer); saved["affinityHiddenPeers"] = hiddenPeers.toList()
            mutable.update { it.copy(requests = it.requests.filterNot { r -> r.person.id == peer }) }
            closeReport()
        }; result }
    }
    private fun action(work: suspend () -> MiraiLinkResult<*>) = mutate { val result = work(); if (result is MiraiLinkResult.Success) load(); result }
    private fun mutate(work: suspend () -> MiraiLinkResult<*>) {
        if (mutable.value.busy) return
        mutable.update { it.copy(busy = true, error = null) }
        mutationJob = viewModelScope.launch {
            val captured = owner
            val result = work()
            if (captured == owner) mutable.update {
                it.copy(busy = false, error = if (result is MiraiLinkResult.Error) result.error.toUiError() else it.error)
            }
        }
    }
}
