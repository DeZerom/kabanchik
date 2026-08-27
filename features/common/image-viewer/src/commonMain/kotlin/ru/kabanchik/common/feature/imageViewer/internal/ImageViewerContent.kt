package ru.kabanchik.common.feature.imageViewer.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage

@Composable
internal fun ImageViewerContent(
    imageUrls: List<String>,
    initialImageIndex: Int,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        if (imageUrls.isNotEmpty()) {
            val pagerState = rememberPagerState(
                initialPage = initialImageIndex,
                pageCount = imageUrls::size,
            )

            HorizontalPager(
                state = pagerState,
                overscrollEffect = null,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                SubcomposeAsyncImage(
                    model = imageUrls[page],
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
