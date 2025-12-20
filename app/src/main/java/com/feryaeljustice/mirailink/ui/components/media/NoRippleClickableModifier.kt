package com.feryaeljustice.mirailink.ui.components.media
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.IntSize

fun Modifier.clickableWithNoRipple(onClick: () -> Unit): Modifier = this.then(NoRippleClickableElement(onClick))

private class NoRippleClickableElement(
    private val onClick: () -> Unit,
) : ModifierNodeElement<NoRippleClickableNode>() {
    override fun create(): NoRippleClickableNode = NoRippleClickableNode(onClick)

    override fun update(node: NoRippleClickableNode) {
        node.onClick = onClick
    }

    override fun hashCode(): Int = onClick.hashCode()

    override fun equals(other: Any?): Boolean = other is NoRippleClickableElement && other.onClick == onClick

    override fun InspectorInfo.inspectableProperties() {
        name = "clickableWithNoRipple"
    }
}

private class NoRippleClickableNode(
    var onClick: () -> Unit,
) : Modifier.Node(),
    PointerInputModifierNode {
    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize,
    ) {
        // Solo nos interesa el paso "Main"
        if (pass != PointerEventPass.Main) return

        pointerEvent.changes.forEach { change ->
            if (change.changedToUpIgnoreConsumed()) {
                onClick()
                change.consume()
            }
        }
    }

    override fun onCancelPointerInput() {
        // No necesitamos hacer nada especial aquí
    }
}
