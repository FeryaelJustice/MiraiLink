package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R

@Composable
fun ThemeSwitcher(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    size: Dp = 48.dp,
    iconSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit
) {
    val offset by animateDpAsState(
        targetValue = if (darkTheme) 0.dp else size,
        animationSpec = animationSpec
    )

    Box(
        modifier = modifier
            .width(size.times(2))
            .height(size)
            .clip(shape = parentShape)
            .clickable { onClick() }
            .background(
                MaterialTheme.colorScheme.secondaryContainer
            )
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(
                    MaterialTheme.colorScheme.primary
                )
        )
        Row(
            modifier = Modifier.border(
                border = BorderStroke(
                    width = borderWidth,
                    color = MaterialTheme.colorScheme.primary
                ), shape = parentShape
            )
        ) {
            Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = painterResource(id = R.drawable.nightlight_24px),
                    tint = if (darkTheme) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.theme_icon)
                )
            }
            Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = painterResource(id = R.drawable.sunny_24px),
                    tint = if (darkTheme) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                    contentDescription = stringResource(R.string.theme_icon)
                )
            }
        }
    }
}

@Preview
@Composable
fun DefaultThemeSwitcherPreview() {
    ThemeSwitcher(darkTheme = false) {}
}