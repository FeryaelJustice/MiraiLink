package com.feryaeljustice.mirailink.ui.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.enum.Gender

@Composable
fun Gender.localizedLabel(): String {
    return when (this) {
        Gender.Male -> stringResource(R.string.gender_male)
        Gender.Female -> stringResource(R.string.gender_female)
        Gender.Other -> stringResource(R.string.gender_other)
    }
}