package com.feryaeljustice.mirailink.domain.model.swipe

import com.feryaeljustice.mirailink.domain.model.user.User

data class UndoSwipeResult(
    val user: User,
    val actionUndone: String,
    val quota: UndoQuota,
)
