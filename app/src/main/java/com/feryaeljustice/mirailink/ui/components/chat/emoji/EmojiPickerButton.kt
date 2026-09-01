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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import dev.alexdametto.compose_emoji_picker.presentation.EmojiPicker

/**
 * Inserta [emoji] en la posicion actual del cursor o reemplaza el texto seleccionado en [TextFieldValue].
 * Posiciona el cursor inmediatamente despues del emoji insertado.
 */
fun TextFieldValue.insertEmojiAtCursor(emoji: String): TextFieldValue {
    val start = selection.min.coerceIn(0, text.length)
    val end = selection.max.coerceIn(0, text.length)
    val newText = text.replaceRange(start, end, emoji)
    val newCursor = start + emoji.length
    return copy(
        text = newText,
        selection = TextRange(newCursor),
        composition = null,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun EmojiPickerButton(
    textFieldValue: TextFieldValue,
    onTextFieldValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    modifierEmojiButtonModifier: Modifier = Modifier,
) {
    EmojiPickerButton(
        onEmojiSelect = { emoji ->
            onTextFieldValueChange(textFieldValue.insertEmojiAtCursor(emoji))
        },
        modifier = modifier,
        modifierEmojiButtonModifier = modifierEmojiButtonModifier,
    )
}

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


