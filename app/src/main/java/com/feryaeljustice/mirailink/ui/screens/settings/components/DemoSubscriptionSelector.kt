package com.feryaeljustice.mirailink.ui.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType

@Composable
fun DemoSubscriptionSelector(isPremium: Boolean, isPlus: Boolean, onPlanSelected: (SubscriptionPlanType) -> Unit) {
    val selected = when {
        isPremium -> SubscriptionPlanType.PREMIUM
        isPlus -> SubscriptionPlanType.PLUS
        else -> SubscriptionPlanType.FREE
    }
    OutlinedCard(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.demo_subscription_title), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.demo_subscription_description), style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SubscriptionPlanType.entries.forEach { plan ->
                    FilterChip(selected = selected == plan, onClick = { onPlanSelected(plan) }, modifier = Modifier.weight(1f),
                        label = { Text(stringResource(when (plan) {
                            SubscriptionPlanType.FREE -> R.string.demo_subscription_free_label
                            SubscriptionPlanType.PLUS -> R.string.demo_subscription_plus_label
                            SubscriptionPlanType.PREMIUM -> R.string.demo_subscription_premium_label
                        })) })
                }
            }
            Text(stringResource(when (selected) {
                SubscriptionPlanType.FREE -> R.string.demo_subscription_free
                SubscriptionPlanType.PLUS -> R.string.demo_subscription_plus
                SubscriptionPlanType.PREMIUM -> R.string.demo_subscription_premium
            }), style = MaterialTheme.typography.bodySmall)
        }
    }
}
