package com.feryaeljustice.mirailink.ui.screens.settings.username

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import com.feryaeljustice.mirailink.ui.utils.toast.showToast

@Suppress("ktlint:standard:function-naming")
@Composable
fun UsernameDetailScreen(
    modifier: Modifier = Modifier,
    username: String,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val profileUrlDisplay = "mirailink.com/user/$username"
    val fullProfileUrl = "https://mirailink.com/user/$username"
    val copiedToastText = stringResource(R.string.username_screen_copied_toast)

    val copyUrlToClipboard = {
        clipboardManager.setText(AnnotatedString(fullProfileUrl))
        showToast(context, copiedToastText, Toast.LENGTH_SHORT)
    }

    val shareProfile = {
        val shareMessage = context.getString(R.string.share_profile_message, username)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.action_share)))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                    Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                } else {
                    Modifier
                },
            )
            .verticalScroll(rememberScrollState()),
    ) {
        // Cabecera con botón de retroceso y título
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiraiLinkIconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            MiraiLinkText(
                text = stringResource(R.string.username_screen_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Contenedor visual tipo campo de texto con la URL (inspirado en la referencia visual de Tinder)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            MiraiLinkText(
                text = stringResource(R.string.username_screen_url_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(16.dp),
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { copyUrlToClipboard() },
                            onLongPress = { copyUrlToClipboard() },
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 18.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    MiraiLinkText(
                        text = profileUrlDisplay,
                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Monospace),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )

                    MiraiLinkIconButton(
                        onClick = shareProfile,
                        modifier = Modifier.size(36.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.action_share),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }

            // Contador de caracteres del identificador
            MiraiLinkText(
                text = "${username.length}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 6.dp, end = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Tarjeta informativa explicativa sobre el identificador único y su permanencia
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(20.dp),
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    MiraiLinkText(
                        text = stringResource(R.string.username_screen_info_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                MiraiLinkText(
                    text = stringResource(R.string.username_screen_info_desc, username),
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    MiraiLinkButton(
                        modifier = Modifier.weight(1f),
                        onClick = copyUrlToClipboard,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        MiraiLinkText(
                            text = stringResource(R.string.username_screen_copy_button),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    MiraiLinkOutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = shareProfile,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        MiraiLinkText(
                            text = stringResource(R.string.action_share),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun UsernameDetailScreenPreview() {
    MaterialTheme {
        UsernameDetailScreen(
            username = "feryaeljustice",
            onBackClick = {},
        )
    }
}
