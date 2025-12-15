package com.feryaeljustice.mirailink.ui.components.bottombars

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.navigation.AppScreen
import com.feryaeljustice.mirailink.ui.navigation.BottomNavItem
import com.feryaeljustice.mirailink.ui.navigation.NavigationState
import com.feryaeljustice.mirailink.ui.navigation.Navigator
import com.skydoves.compose.stability.runtime.TraceRecomposition

@Suppress("ParamsComparedByRef")
@TraceRecomposition
@Composable
fun MiraiLinkBottomBar(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    navState: NavigationState,
    enabled: Boolean = true,
    onDestinationClick: (BottomNavItem) -> Unit,
) {
    val bottomNavDestinations =
        listOf(
            BottomNavItem(AppScreen.HomeScreen, R.drawable.ic_home, R.string.nav_home),
            BottomNavItem(AppScreen.MessagesScreen, R.drawable.ic_chat, R.string.nav_messages),
            BottomNavItem(AppScreen.ProfileScreen, R.drawable.ic_user, R.string.nav_profile),
        )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    ) {
        bottomNavDestinations.forEach { item ->
            // En Nav3, “tab seleccionado” es el topLevelRoute actual
            val selected = (navState.topLevelRoute == item.appScreen)

            NavigationBarItem(
                selected = selected,
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onTertiary,
                        selectedTextColor = MaterialTheme.colorScheme.onTertiary,
                        indicatorColor = MaterialTheme.colorScheme.outline,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                onClick = {
                    onDestinationClick(item)
                    if (enabled && !selected) {
                        // En Nav3 no hay popUpTo/startDestinationId aquí.
                        // Cambias de stack/top-level route y listo.
                        navigator.navigate(item.appScreen)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.label),
                        tint = if (selected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface,
                    )
                },
                label = {
                    MiraiLinkText(
                        text = stringResource(id = item.label),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall,
                    )
                },
            )
        }
    }
}
