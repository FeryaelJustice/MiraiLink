package com.feryaeljustice.mirailink.ui.components.molecules

import android.content.ClipData
import android.os.Build
import android.os.PersistableBundle
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.remote.interceptor.DebugSecurityLinks
import com.feryaeljustice.mirailink.data.remote.interceptor.DebugSecurityLink
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun DebugSecurityLinkNotice() {
    if (!BuildConfig.DEBUG) return
    val clipboard = LocalClipboard.current
    val progress = remember { Animatable(1f) }
    var link by remember { mutableStateOf<DebugSecurityLink?>(null) }
    var copied by remember { mutableStateOf(false) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    LaunchedEffect(Unit) {
        DebugSecurityLinks.events.collectLatest { value ->
            copied = false
            link = value
            progress.snapTo(1f)
            progress.animateTo(0f, animationSpec = tween(10_000))
            link = null
        }
    }
    LaunchedEffect(copied) {
        if (copied) { delay(3_000); copied = false }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        val current = link
        if (current != null || copied) {
            Surface(Modifier.fillMaxWidth().padding(16.dp), shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.inverseSurface, tonalElevation = 6.dp) {
                Column {
                    if (current != null && !current.mailDelivered) {
                        Text(stringResource(R.string.debug_mail_not_delivered), Modifier.padding(start = 16.dp, top = 8.dp), color = MaterialTheme.colorScheme.inverseOnSurface, style = MaterialTheme.typography.labelMedium)
                    }
                    Row(Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(if (copied) stringResource(R.string.debug_link_copied) else current?.url.orEmpty(), Modifier.weight(1f), color = MaterialTheme.colorScheme.inverseOnSurface, maxLines = 3)
                        if (current != null) TextButton(onClick = {
                            scope.launch {
                                val clip = ClipData.newPlainText("MiraiLink", current.url)
                                if (Build.VERSION.SDK_INT >= 33) clip.description.extras = PersistableBundle().apply { putBoolean("android.content.extra.IS_SENSITIVE", true) }
                                val copiedSuccessfully = runCatching { clipboard.setClipEntry(clip.toClipEntry()) }.isSuccess
                                if (copiedSuccessfully && link == current) {
                                    link = null
                                    copied = true
                                }
                            }
                        }) { Text(stringResource(R.string.debug_link_copy)) }
                    }
                    if (current != null) LinearProgressIndicator(progress = { progress.value }, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
