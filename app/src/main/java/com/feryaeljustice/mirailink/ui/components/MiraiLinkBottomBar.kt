package com.feryaeljustice.mirailink.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.navigation.AppScreen
import com.feryaeljustice.mirailink.ui.navigation.BottomNavItem

@Composable
fun MiraiLinkBottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentDestination: NavDestination?,
    enabled: Boolean = true,
    onDestinationClick: (BottomNavItem) -> Unit
) {
    val bottomNavDestinations = listOf(
        BottomNavItem(AppScreen.HomeScreen, R.drawable.ic_home, R.string.nav_home),
        BottomNavItem(AppScreen.MessagesScreen, R.drawable.ic_chat, R.string.nav_messages),
        BottomNavItem(AppScreen.ProfileScreen, R.drawable.ic_user, R.string.nav_profile)
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    ) {
        bottomNavDestinations.forEach { item ->
            val selected =
                currentDestination?.hierarchy?.any { it.hasRoute(item.appScreen::class) } == true
            NavigationBarItem(
                selected = selected,
                colors = NavigationBarItemDefaults.colors(
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
                        navController.navigate(item.appScreen) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.label),
                        tint = if (selected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.label),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }
}

@Preview(
    name = "MiraiLinkBottomBarPreview",
    showBackground = true,
    widthDp = 410,
    heightDp = 128
)
@Preview(
    name = "MiraiLinkBottomBarPreview",
    showBackground = true,
    widthDp = 457,
    heightDp = 118
)
@Preview(showBackground = true)
@Composable
fun MiraiLinkBottomBarPreview() {
    val navController = rememberNavController()
    // You can pass null for currentDestination or a mock NavDestination
    // For a more complete preview, you might want to simulate a current destination
    MiraiLinkBottomBar(
        navController = navController,
        currentDestination = null,
        onDestinationClick = {})
}


