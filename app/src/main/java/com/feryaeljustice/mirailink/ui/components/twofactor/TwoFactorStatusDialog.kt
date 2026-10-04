package com.feryaeljustice.mirailink.ui.components.twofactor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme

@Composable
fun TwoFactorStatusDialog(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onDismiss: () -> Unit,
    onEnable: () -> Unit,
    onDisable: () -> Unit,
) {
    MiraiLinkDialog(
        modifier = modifier,
        title = stringResource(R.string.configure_two_factor),
        onDismiss = onDismiss,
        showAcceptButton = false,
        showCancelButton = false,
        iconContent = {
            val iconBg = if (enabled) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            val iconTint = if (enabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
            val iconVector = if (enabled) {
                Icons.Default.CheckCircle
            } else {
                Icons.Default.Lock
            }
            val contentDesc = stringResource(
                if (enabled) {
                    R.string.two_factor_status_icon_active_desc
                } else {
                    R.string.two_factor_status_icon_inactive_desc
                },
            )

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(iconBg)
                    .semantics { contentDescription = contentDesc },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(30.dp),
                )
            }
        },
        titleContent = {
            MiraiLinkText(
                text = stringResource(R.string.configure_two_factor),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        messageContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        // Status badge pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (enabled) {
                                        MaterialTheme.colorScheme.primaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
                                    },
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (enabled) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.error
                                        },
                                    ),
                            )
                            MiraiLinkText(
                                text = stringResource(
                                    if (enabled) {
                                        R.string.two_factor_status_active
                                    } else {
                                        R.string.two_factor_status_inactive
                                    },
                                ),
                                color = if (enabled) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.error
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        // Status description
                        MiraiLinkText(
                            text = stringResource(
                                if (enabled) {
                                    R.string.two_factor_enabled_message
                                } else {
                                    R.string.two_factor_disabled_message
                                },
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        // Informative tip
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            )
                            MiraiLinkText(
                                text = stringResource(
                                    if (enabled) {
                                        R.string.two_factor_status_active_tip
                                    } else {
                                        R.string.two_factor_status_inactive_tip
                                    },
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                            )
                        }
                    }
                }
            }
        },
        confirmButtonContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (enabled) {
                    MiraiLinkButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = onDismiss,
                    ) {
                        MiraiLinkText(
                            text = stringResource(R.string.close),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    MiraiLinkOutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = onDisable,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        MiraiLinkText(
                            text = stringResource(R.string.disable_two_factor),
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                } else {
                    MiraiLinkButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = onEnable,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        MiraiLinkText(
                            text = stringResource(R.string.activate_two_factor),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    MiraiLinkOutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = onDismiss,
                    ) {
                        MiraiLinkText(
                            text = stringResource(R.string.close),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        },
    )
}

@Preview(name = "Two Factor Status - Enabled", showBackground = true)
@Composable
private fun TwoFactorStatusDialogEnabledPreview() {
    MiraiLinkTheme {
        TwoFactorStatusDialog(
            enabled = true,
            onDismiss = {},
            onEnable = {},
            onDisable = {},
        )
    }
}

@Preview(name = "Two Factor Status - Disabled", showBackground = true)
@Composable
private fun TwoFactorStatusDialogDisabledPreview() {
    MiraiLinkTheme {
        TwoFactorStatusDialog(
            enabled = false,
            onDismiss = {},
            onEnable = {},
            onDisable = {},
        )
    }
}
