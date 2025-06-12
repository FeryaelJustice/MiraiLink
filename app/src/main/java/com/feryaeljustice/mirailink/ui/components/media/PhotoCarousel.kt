package com.feryaeljustice.mirailink.ui.components.media

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.getFormattedUrl
import kotlinx.coroutines.launch

@Composable
fun PhotoCarousel(photoUrls: List<String>, onLongPressOnImage: (String) -> Unit) {
    val scope = rememberCoroutineScope()

    val images = photoUrls.ifEmpty { listOf(R.drawable.logomirailink.toString()) }

    val pagerState = rememberPagerState(pageCount = { images.size })
//    val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()

    val pageInteractionSource = remember { MutableInteractionSource() }
//    val pageIsPressed by pageInteractionSource.collectIsPressedAsState()

    // Stop auto-advancing when pager is dragged or one of the pages is pressed
//    val autoAdvance = !pagerIsDragged && !pageIsPressed
//
//    if (autoAdvance) {
//        LaunchedEffect(pagerState, pageInteractionSource) {
//            while (true) {
//                delay(2000)
//                val nextPage = (pagerState.currentPage + 1) % images.size
//                pagerState.animateScrollToPage(nextPage)
//            }
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            key = { images[it] },
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            val url = images[page].getFormattedUrl()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .placeholder(drawableResId = R.drawable.logomirailink)
                    .build(),
                contentDescription = stringResource(
                    R.string.content_description_photo_carousel_pager_image,
                    page + 1
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .clickable(
                        interactionSource = pageInteractionSource,
                        indication = LocalIndication.current
                    ) {
                        scope.launch {
                            val nextPage = (pagerState.currentPage + 1) % images.size
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }
                    .pointerInput(url) {
                        detectTapGestures(
                            onLongPress = {
                                onLongPressOnImage(url)
                            }
                        )
                    }
            )
        }

        PagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            activeColor = Color.White,
            inactiveColor = Color.LightGray,
        )
    }
}

@Composable
private fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.LightGray
) {
    val pageCount = pagerState.pageCount
    val currentPage = pagerState.currentPage

    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(10.dp)
            )
        }
    }
}