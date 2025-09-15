package com.example.galaxygram.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.galaxygram.ui.theme.CardGradient
import com.example.galaxygram.ui.theme.DateChipBg
import com.example.galaxygram.ui.theme.MediaBlue

@Composable
fun ApodCard(
    title: String,
    date: String,
    imageUrl: String?,
    isVideo: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Box {
            val painter = rememberAsyncImagePainter(model = imageUrl)
            val isLoading = painter.state is AsyncImagePainter.State.Loading

            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp)
                    .aspectRatio(1f)
                    .drawWithContent {
                        drawContent()
                        drawRect(CardGradient)
                    }
            )

            if (isLoading) {
                Box(
                    Modifier
                        .matchParentSize()
                        .padding(1.dp)
                ) {  }
            }

            AssistChip(
                onClick = {},
                enabled = false,
                label = { Text(date) },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = DateChipBg,
                    labelColor = MaterialTheme.colorScheme.onSurface
                )
            )

            if (isVideo) {
                AssistChip(
                    onClick = {},
                    enabled = false,
                    label = { Text("VIDEO") },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MediaBlue,
                        labelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
