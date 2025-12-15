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

    fun navigateUp(): Boolean {
        val stack = state.backStacks.getValue(state.topLevelRoute)
        return if (stack.size > 1) {
            stack.removeLast()
            true
        } else {
            false
        }
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

    fun backTo(targetRoute: NavKey) {
        val currentStack = state.backStacks[state.topLevelRoute] ?: error("Stack for ${state.topLevelRoute}not found")
        val currentRoute = currentStack.last()

        if (currentStack.isEmpty()) return
        if (targetRoute !in currentStack) return

        while (currentStack.isNotEmpty() && currentRoute != targetRoute) {
            currentStack.removeLastOrNull()
        }
    }

    fun resetToTopLevel(
        topLevel: NavKey,
        firstChild: NavKey? = null,
    ) {
        require(topLevel in state.backStacks.keys) { "TopLevel $topLevel no está en backStacks" }
        val stack = state.backStacks.getValue(topLevel)
        stack.clear()
        stack.add(topLevel)
        if (firstChild != null && firstChild !in state.backStacks.keys) {
            stack.add(firstChild)
        }
        state.topLevelRoute = topLevel
    }

    fun resetAllToStart() {
        state.backStacks.forEach { (key, stack) ->
            stack.clear()
            stack.add(key)
        }
        state.topLevelRoute = state.startRoute
    }
}
