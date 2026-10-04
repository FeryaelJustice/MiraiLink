package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.enum.Gender

@Suppress("ktlint:standard:function-naming")
@Composable
fun GenderSelector(
    modifier: Modifier = Modifier,
    gender: Gender,
    onChange: (Gender) -> Unit,
) {
    val genderMaleText = stringResource(R.string.gender_male)
    val genderFemaleText = stringResource(R.string.gender_female)
    val itemLabel: (Gender) -> String = { g ->
        when (g) {
            Gender.Male -> genderMaleText
            Gender.Female -> genderFemaleText
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

@Preview(showBackground = true)
@Composable
private fun GenderSelectorPreview() {
    GenderSelector(
        gender = Gender.Male,
        onChange = {},
    )
}

