package com.feryaeljustice.mirailink.ui.components.chat.emoji

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.superCapitalize
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.utils.toast.showToast
import com.makeappssimple.abhimanyu.composeemojipicker.ComposeEmojiPickerBottomSheetUI

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiPickerButton(
    onEmojiSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    modifierEmojiButtonModifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isModalBottomSheetVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    if (isModalBottomSheetVisible) {
        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            shape = RectangleShape,
            tonalElevation = 0.dp,
            onDismissRequest = {
                isModalBottomSheetVisible = false
                searchText = ""
            },
            dragHandle = null,
            contentWindowInsets = {
                WindowInsets(0.dp)
            },
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                ComposeEmojiPickerBottomSheetUI(
                    onEmojiClick = { emoji ->
                        onEmojiSelect(emoji.character)
                        isModalBottomSheetVisible = false
                    },
                    onEmojiLongClick = { emoji ->
                        showToast(
                            context,
                            emoji.unicodeName.superCapitalize(),
                            Toast.LENGTH_SHORT,
                        )
                    },
                    searchText = searchText,
                    updateSearchText = { updatedSearchText -> searchText = updatedSearchText },
                )
            }
        }
    }

    MiraiLinkIconButton(
        modifier = modifierEmojiButtonModifier,
        onClick = { isModalBottomSheetVisible = true },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_emoji),
            contentDescription = stringResource(R.string.emoji),
        )
    }
}
