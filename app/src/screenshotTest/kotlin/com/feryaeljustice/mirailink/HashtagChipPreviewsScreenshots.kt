package com.feryaeljustice.mirailink

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.feryaeljustice.mirailink.ui.components.molecules.HashtagChip

class HashtagChipPreviewsScreenshots {

    @Preview(showBackground = true)
    @Composable
    fun HashtagChipPreview(){
        HashtagChip(text = "hola", selected = true)
    }
}