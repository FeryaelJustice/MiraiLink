package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.enum.Gender

@Composable
fun GenderSelector(
    gender: Gender,
    onChange: (Gender) -> Unit
) {
    val ctx = LocalContext.current
    val itemLabel: (Gender) -> String = { g ->
        when (g) {
            Gender.Male -> ctx.getString(R.string.gender_male)
            Gender.Female -> ctx.getString(R.string.gender_female)
            Gender.Other -> ctx.getString(R.string.gender_other)
        }
    }

    MiraiLinkSimpleDropdown(
        label = stringResource(R.string.gender),
        options = Gender.entries,
        selected = gender,
        onSelected = onChange,
        itemLabel = itemLabel
    )
}