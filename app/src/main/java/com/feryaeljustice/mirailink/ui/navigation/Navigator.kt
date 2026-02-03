package com.feryaeljustice.mirailink.ui.navigation

import androidx.navigation3.runtime.NavKey

class Navigator(
    val state: NavigationState,
) {
    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            // top-level: cambia de “stack”
            state.topLevelRoute = route
            return
        }
        // no top-level: push al stack actual
        val stack = state.backStacks.getValue(state.topLevelRoute)
        stack.add(route)
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute] ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }

    fun resetToTopLevel(
        topLevel: NavKey,
        firstChild: NavKey? = null,
    ) {
        require(topLevel in state.backStacks.keys) { "TopLevel $topLevel no está en backStacks" }

        state.backStacks.forEach { (key, stack) ->
            stack.clear()
            stack.add(key)
        }

        if (firstChild != null && firstChild !in state.backStacks.keys) {
            state.backStacks.getValue(topLevel).add(firstChild)
        }
        state.topLevelRoute = topLevel
    }
}
