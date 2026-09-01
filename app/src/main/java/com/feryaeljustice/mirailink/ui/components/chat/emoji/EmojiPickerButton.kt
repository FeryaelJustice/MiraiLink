package com.feryaeljustice.mirailink.ui.components.chat.emoji

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import dev.alexdametto.compose_emoji_picker.presentation.EmojiPicker

@Suppress("ktlint:standard:function-naming")
@Composable
fun EmojiPickerButton(
    onEmojiSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    modifierEmojiButtonModifier: Modifier = Modifier,
) {
    var isEmojiPickerOpen by remember { mutableStateOf(false) }

    EmojiPicker(
        open = isEmojiPickerOpen,
        onClose = {
            isEmojiPickerOpen = false
        },
        onEmojiSelected = { emoji ->
            onEmojiSelect(emoji.emoji)
            isEmojiPickerOpen = false
        },
    )

    MiraiLinkIconButton(
        modifier = modifierEmojiButtonModifier.then(modifier),
        onClick = { isEmojiPickerOpen = true },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_emoji),
            contentDescription = stringResource(R.string.emoji),
        )
    }
}

