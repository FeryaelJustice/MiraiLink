package com.feryaeljustice.mirailink.ui.components.chat.emoji

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EmojiCursorPositionTest {

    @Test
    fun insertEmojiAtCursor_whenCursorAtBeginning_insertsAtStartAndMovesCursor() {
        val initial = TextFieldValue(text = "World", selection = TextRange(0))
        val result = initial.insertEmojiAtCursor("👋")

        assertThat(result.text).isEqualTo("👋World")
        assertThat(result.selection).isEqualTo(TextRange("👋".length))
    }

    @Test
    fun insertEmojiAtCursor_whenCursorInMiddle_insertsAtCursorPosition() {
        val initial = TextFieldValue(text = "Hello World", selection = TextRange(5))
        val result = initial.insertEmojiAtCursor("✨")

        assertThat(result.text).isEqualTo("Hello✨ World")
        assertThat(result.selection).isEqualTo(TextRange(5 + "✨".length))
    }

    @Test
    fun insertEmojiAtCursor_whenCursorAtEnd_appendsAndPositionsCursorAtEnd() {
        val initial = TextFieldValue(text = "Hello", selection = TextRange(5))
        val result = initial.insertEmojiAtCursor("😀")

        assertThat(result.text).isEqualTo("Hello😀")
        assertThat(result.selection).isEqualTo(TextRange(5 + "😀".length))
    }

    @Test
    fun insertEmojiAtCursor_whenTextSelected_replacesSelectionWithEmoji() {
        val initial = TextFieldValue(text = "Hello REPLACE_ME World", selection = TextRange(6, 16))
        val result = initial.insertEmojiAtCursor("🎉")

        assertThat(result.text).isEqualTo("Hello 🎉 World")
        assertThat(result.selection).isEqualTo(TextRange(6 + "🎉".length))
    }

    @Test
    fun insertEmojiAtCursor_whenEmpty_insertsEmojiAndPositionsCursor() {
        val initial = TextFieldValue(text = "", selection = TextRange(0))
        val result = initial.insertEmojiAtCursor("🌸")

        assertThat(result.text).isEqualTo("🌸")
        assertThat(result.selection).isEqualTo(TextRange("🌸".length))
    }
}
