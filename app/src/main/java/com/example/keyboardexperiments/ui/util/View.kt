package com.example.keyboardexperiments.ui.util

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.google.android.material.internal.ViewUtils.doOnApplyWindowInsets

/**
 * Sets up IME inset handling. The [onImeInsetAnimation] updates whenever ime insets are changing. The window insets
 * listener exposes a way to manually handle window insets when they are being applied.
 */
fun View.doOnAnimateImeInset(
    onImeInsetAnimation: (Int) -> Unit,
    shouldConsume: Boolean,
    windowInsetsListener: (
        insetView: View,
        windowInsets: WindowInsetsCompat,
        initialPadding: Rect,
        initialMargins: Rect,
    ) -> Unit = { _, _, _, _ -> },
) {
    val initialPadding = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)
    val initialMargins = Rect(marginLeft, marginTop, marginRight, marginBottom)
    val insetListener = ImeProgressWindowInsetAnimation(
        onImeInsetAnimation = onImeInsetAnimation,
        windowInsetsListener = {
            windowInsetsListener(this@doOnAnimateImeInset, it, initialPadding, initialMargins)
        },
    )
    ViewCompat.setWindowInsetsAnimationCallback(this, insetListener)
    doOnApplyWindowInsets(shouldConsume = shouldConsume) { insetView, windowInsets, _, _ ->
        // Forward this to the inset listener, it will handle everything internally.
        insetListener.onApplyWindowInsets(insetView, windowInsets)
    }
}

/**
 * A helper method wrapping [ViewCompat.setOnApplyWindowInsetsListener], additionally providing the initial padding
 * and margins of the view.
 *
 * If [shouldConsume] is true, this method consumes insets and returns the remaining unconsumed insets. Otherwise,
 * this method does not consume any window insets, allowing any and all children to receive the same insets.
 *
 * This is a `set` listener, so only the last [windowInsetsListener] applied by [doOnApplyWindowInsets] will be ran.
 *
 * This approach was based on [https://medium.com/androiddevelopers/windowinsets-listeners-to-layouts-8f9ccc8fa4d1]
 */
fun View.doOnApplyWindowInsets(
    shouldConsume: Boolean = false,
    windowInsetsListener: (
        insetView: View,
        windowInsets: WindowInsetsCompat,
        initialPadding: Rect,
        initialMargins: Rect,
    ) -> Unit,
) {
    val initialPadding = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)
    val initialMargins = Rect(marginLeft, marginTop, marginRight, marginBottom)

    ViewCompat.setOnApplyWindowInsetsListener(this) { insetView, windowInsets ->
        windowInsetsListener(insetView, windowInsets, initialPadding, initialMargins)
        if (shouldConsume) WindowInsetsCompat.CONSUMED else windowInsets
    }

    // Whenever a view is detached and then re-attached to the screen, we need to apply insets again.
    //
    // In particular, it is not enough to apply insets only on the first attach:
    //
    // doOnAttach { requestApplyInsets() }
    //
    // For example, considering the following scenario:
    // - A RecyclerView lays out items while in landscape.
    // - Some items that depend on the insets are laid out, and are then detached because they go off-screen.
    // - The user rotates the device 180 degrees. This is still landscape, so no configuration change occurs, but an
    // inset change _does_ occur.
    // - The detached items are reattached because they come back on-screen.
    //
    // At this point, the insets applied to the view would be out of date, and they wouldn't be updated, since the view
    // was already attached once, and the callback for the new insets caused by the rotation would have already been
    // applied, and skipped updating the detached view.
    addOnAttachStateChangeListener(
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        },
    )

    // If the view is already attached, immediately request insets be applied.
    if (isAttachedToWindow) {
        requestApplyInsets()
    }
}
