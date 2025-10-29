package com.feryaeljustice.mirailink.data.mappers.ui

import com.feryaeljustice.mirailink.domain.model.VersionCheckResult
import com.feryaeljustice.mirailink.ui.viewentries.VersionCheckResultViewEntry

fun VersionCheckResult.toVersionCheckResultViewEntry(): VersionCheckResultViewEntry =
    VersionCheckResultViewEntry(
        mustUpdate = mustUpdate,
        shouldUpdate = shouldUpdate,
        message = message,
        playStoreUrl = playStoreUrl
    )