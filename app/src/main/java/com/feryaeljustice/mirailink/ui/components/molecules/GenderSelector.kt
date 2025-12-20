package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.enum.Gender

@Suppress("ktlint:standard:function-naming")
@Composable
fun GenderSelector(
    gender: Gender,
    onChange: (Gender) -> Unit,
    modifier: Modifier = Modifier,
) {
    val genderMaleText = stringResource(R.string.gender_male)
    val genderFemaleText = stringResource(R.string.gender_female)
    val genderOtherText = stringResource(R.string.gender_other)
    val itemLabel: (Gender) -> String = { g ->
        when (g) {
            Gender.Male -> genderMaleText
            Gender.Female -> genderFemaleText
            Gender.Other -> genderOtherText
        }
    }

    MiraiLinkSimpleDropdown(
        modifier = modifier,
        label = stringResource(R.string.gender),
        options = Gender.entries,
        selected = gender,
        onSelect = onChange,
        itemLabel = itemLabel,
    )
}
