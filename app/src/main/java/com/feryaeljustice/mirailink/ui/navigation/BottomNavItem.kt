package com.feryaeljustice.mirailink.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class BottomNavItem(
    val appScreen: AppScreen,
    val icon: Int,
    val label: Int,
)
