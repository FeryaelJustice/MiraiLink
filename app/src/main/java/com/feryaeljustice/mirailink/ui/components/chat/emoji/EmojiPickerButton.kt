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
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.superCapitalize
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.makeappssimple.abhimanyu.composeemojipicker.ComposeEmojiPickerBottomSheetUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiPickerButton(
    onEmojiSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isModalBottomSheetVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    if (isModalBottomSheetVisible) {
        ModalBottomSheet(
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
                modifier = Modifier.fillMaxSize()
            ) {
                ComposeEmojiPickerBottomSheetUI(
                    onEmojiClick = { emoji ->
                        onEmojiSelected(emoji.character)
                        isModalBottomSheetVisible = false
                    },
                    onEmojiLongClick = { emoji ->
                        Toast.makeText(
                            context,
                            emoji.unicodeName.superCapitalize(),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    searchText = searchText,
                    updateSearchText = { updatedSearchText -> searchText = updatedSearchText },
                )
            }
        }
    }

    MiraiLinkIconButton(
        onClick = { isModalBottomSheetVisible = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_emoji),
            contentDescription = "Emoji"
        )
    }
}
