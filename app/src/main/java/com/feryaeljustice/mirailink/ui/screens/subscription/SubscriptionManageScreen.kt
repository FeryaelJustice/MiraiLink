package com.feryaeljustice.mirailink.ui.screens.subscription

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog
import com.feryaeljustice.mirailink.ui.error.asString
import com.feryaeljustice.mirailink.ui.utils.toast.showToast
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun SubscriptionManageScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToPaywall: () -> Unit = {},
    viewModel: SubscriptionManageViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.cancelPlayStoreUrl) {
        uiState.cancelPlayStoreUrl?.let { url ->
            try {
                uriHandler.openUri(url)
            } catch (_: Exception) {
                uriHandler.openUri("https://play.google.com/store/account/subscriptions")
            }
            viewModel.clearCancelUrl()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { err ->
            showToast(context, err.asString(context), Toast.LENGTH_LONG)
            viewModel.clearError()
        }
    }

    if (showCancelDialog) {
        MiraiLinkDialog(
            title = stringResource(R.string.subscription_manage_cancel_dialog_title),
            message = stringResource(R.string.subscription_manage_cancel_dialog_message),
            onDismiss = { showCancelDialog = false },
            onAccept = {
                showCancelDialog = false
                viewModel.requestCancelIntent()
            },
            onCancel = { showCancelDialog = false },
            acceptText = stringResource(R.string.subscription_manage_cancel_dialog_confirm),
            cancelText = stringResource(R.string.cancel),
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                MiraiLinkText(
                    text = stringResource(R.string.subscription_manage_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                MiraiLinkText(
                    text = stringResource(R.string.subscription_manage_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subscription overview card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            ),
        ) {
            val isPremium = uiState.subscriptionInfo.isPremium
            val isPlus = uiState.subscriptionInfo.isPlus && !isPremium
            val planName = when {
                isPremium -> stringResource(R.string.subscription_plan_premium_name)
                isPlus -> stringResource(R.string.subscription_plan_plus_name)
                else -> stringResource(R.string.subscription_plan_free_name)
            }
            val accentColor = if (isPlus) Color(0xFF00E5FF) else Color(0xFFFFB300)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = accentColor,
                            modifier = Modifier.size(42.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_bolt),
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(22.dp),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        MiraiLinkText(
                            text = planName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = accentColor,
                    ) {
                        MiraiLinkText(
                            text = if (uiState.subscriptionInfo.isPremium || uiState.subscriptionInfo.isPlus) {
                                stringResource(R.string.subscription_manage_status_active)
                            } else {
                                stringResource(R.string.subscription_manage_status_inactive)
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                uiState.subscriptionInfo.expiresAt?.let { expiration ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        MiraiLinkText(
                            text = stringResource(R.string.subscription_manage_next_renewal_label),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        MiraiLinkText(
                            text = expiration.take(10),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                MiraiLinkText(
                    text = stringResource(R.string.subscription_manage_billing_service),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Active perks card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                val isPlus = uiState.subscriptionInfo.isPlus && !uiState.subscriptionInfo.isPremium
                MiraiLinkText(
                    text = if (isPlus) {
                        stringResource(R.string.subscription_plan_plus_desc)
                    } else {
                        stringResource(R.string.subscription_plan_premium_desc)
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )

                if (isPlus) {
                    ActivePerkRow(text = stringResource(R.string.subscription_paywall_perks_plus_ads))
                    ActivePerkRow(text = stringResource(R.string.subscription_paywall_perks_plus_radius))
                    ActivePerkRow(text = stringResource(R.string.subscription_paywall_perks_plus_likes))
                    ActivePerkRow(text = stringResource(R.string.subscription_paywall_perks_plus_badge))
                } else {
                    ActivePerkRow(text = stringResource(R.string.subscription_paywall_perks_premium_plus_all))
                    ActivePerkRow(text = stringResource(R.string.subscription_paywall_perks_premium_likes))
                    ActivePerkRow(text = stringResource(R.string.subscription_paywall_perks_premium_passport))
                    ActivePerkRow(text = stringResource(R.string.subscription_paywall_perks_premium_badge))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // If user is on Plus, offer direct upgrade to Premium
        if (uiState.subscriptionInfo.isPlus && !uiState.subscriptionInfo.isPremium) {
            androidx.compose.material3.Button(
                onClick = onNavigateToPaywall,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB300),
                    contentColor = Color.Black,
                ),
            ) {
                MiraiLinkText(
                    text = stringResource(R.string.subscription_manage_upgrade_button),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Cancel subscription button
        OutlinedButton(
            onClick = { showCancelDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !uiState.isCanceling,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error,
            ),
        ) {
            if (uiState.isCanceling) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                MiraiLinkText(
                    text = stringResource(R.string.subscription_manage_cancel_button),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun ActivePerkRow(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(24.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        MiraiLinkText(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}
