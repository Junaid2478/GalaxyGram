package com.example.galaxygram.feature.detail

import android.content.Intent
import android.net.Uri
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodDetailScreen(
    title: String,
    date: String,
    explanation: String,
    imageUrl: String?,
    hdUrl: String?,
    isVideo: Boolean,
    videoUrl: String?,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current
    var webError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { ApodDetailTopBar(title = title, onBack = onBack) },
        floatingActionButton = {
            when {
                isVideo && !videoUrl.isNullOrBlank() -> {
                    ExtendedFloatingActionButton(
                        text = { Text("Open Video") },
                        icon = { Icon(Icons.Filled.OpenInNew, null) },
                        onClick = { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))) }
                    )
                }
                !hdUrl.isNullOrBlank() -> {
                    ExtendedFloatingActionButton(
                        text = { Text("Open HD") },
                        icon = { Icon(Icons.Filled.OpenInNew, null) },
                        onClick = { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(hdUrl))) }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (isVideo && !videoUrl.isNullOrBlank()) {
                val raw = videoUrl!!
                when {
                    isMp4(raw) -> VideoPlayer(url = raw)
                    isYouTubeOrVimeo(raw) -> {
                        val embed = toEmbeddableUrl(raw)
                        WebBlock(urlToLoad = embed, onWebError = { webError = it })
                    }
                    isApodHtml(raw) -> {
                        WebBlock(urlToLoad = raw, onWebError = { webError = it })
                    }
                }
            } else {
                AsyncImage(
                    model = imageUrl ?: hdUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 280.dp)
                )
            }

            if (webError != null) {
                Text(
                    "This video can’t be embedded here. Tap to open externally.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Button(
                    onClick = { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl ?: hdUrl ?: ""))) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) { Text("Open Video") }
            }

            Spacer(Modifier.height(16.dp))
            Text(text = date, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(8.dp))
            Text(text = explanation, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            Spacer(Modifier.height(48.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodDetailTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } }
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoPlayer(url: String) {
    val context = LocalContext.current
    val player = remember(url) {
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(
                androidx.media3.exoplayer.source.DefaultMediaSourceFactory(
                    androidx.media3.datasource.DefaultDataSource.Factory(
                        context,
                        androidx.media3.datasource.DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
                    )
                )
            )
            .build().apply {
                setMediaItem(MediaItem.fromUri(url))
                prepare()
                playWhenReady = false
            }
    }
    DisposableEffect(player) { onDispose { player.release() } }
    AndroidView(
        modifier = Modifier.fillMaxWidth().heightIn(min = 280.dp),
        factory = { ctx -> PlayerView(ctx).apply { this.player = player; useController = true } }
    )
}

private fun isMp4(url: String?) = url?.contains(".mp4", ignoreCase = true) == true

private fun isYouTubeOrVimeo(url: String?): Boolean {
    val u = url?.lowercase() ?: return false
    return listOf("youtube.com", "youtu.be", "m.youtube.com", "youtube-nocookie.com", "vimeo.com", "player.vimeo.com").any { u.contains(it) }
}

private fun isApodHtml(url: String?) =
    url?.lowercase()?.let { it.contains("apod.nasa.gov/apod/") && it.endsWith(".html") } == true

private fun toEmbeddableUrl(src: String): String {
    val lower = src.lowercase()
    val uri = android.net.Uri.parse(src)
    if (lower.contains("youtube.com") && lower.contains("/embed/")) return src
    if (lower.contains("player.vimeo.com")) return src
    if (lower.contains("youtube.com") || lower.contains("m.youtube.com") || lower.contains("youtube-nocookie.com")) {
        if (lower.contains("/shorts/")) {
            val id = src.substringAfter("/shorts/").substringBefore("?").substringBefore("/")
            if (id.isNotBlank()) return "https://www.youtube.com/embed/$id"
        }
        uri.getQueryParameter("v")?.let { v -> if (v.isNotBlank()) return "https://www.youtube.com/embed/$v" }
    }
    if (lower.contains("youtu.be/")) {
        val id = src.substringAfter("youtu.be/").substringBefore("?").substringBefore("/")
        if (id.isNotBlank()) return "https://www.youtube.com/embed/$id"
    }
    if (lower.contains("vimeo.com") && !lower.contains("player.vimeo.com")) {
        val id = (uri.path ?: "").split('/').firstOrNull { it.matches(Regex("\\d+")) }
        if (!id.isNullOrBlank()) return "https://player.vimeo.com/video/$id"
    }
    return src
}

@Composable
private fun WebBlock(urlToLoad: String, onWebError: (String) -> Unit) {
    AndroidView(
        modifier = Modifier.fillMaxWidth().height(280.dp),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
                val cm = android.webkit.CookieManager.getInstance()
                cm.setAcceptCookie(true)
                cm.setAcceptThirdPartyCookies(this, true)
                webChromeClient = object : android.webkit.WebChromeClient() {}
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(v: WebView, url: String) {}
                    override fun onReceivedError(v: WebView, req: android.webkit.WebResourceRequest, err: android.webkit.WebResourceError) {
                        onWebError(err.description?.toString() ?: "WebView error")
                    }
                }
                loadUrl(urlToLoad)
            }
        },
        update = { it.loadUrl(urlToLoad) }
    )
}


