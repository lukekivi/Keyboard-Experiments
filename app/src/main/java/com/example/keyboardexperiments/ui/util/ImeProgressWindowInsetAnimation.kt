package com.example.keyboardexperiments.ui.util

import android.os.Build
import android.view.View
import android.view.WindowInsetsAnimation
import androidx.core.graphics.Insets
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

/**
 * A [WindowInsetsAnimation.Callback] and [OnApplyWindowInsetsListener] for nicely handling IME animations.
 *
 * When the keyboard is opened and closed, [onImeInsetAnimation] will be called with an int value between 0 and
 * the maximum height of the keyboard.
 *
 * [windowInsetsListener] will be forwarded on by the implementation of [OnApplyWindowInsetsListener].
 *
 * This approach is loosely based on the
 * [WindowInsetsAnimation](https://github.com/android/user-interface-samples/tree/master/WindowInsetsAnimation) sample.
 */
class ImeProgressWindowInsetAnimation(
    private val onImeInsetAnimation: (Int) -> Unit,
    private val windowInsetsListener: (insets: WindowInsetsCompat) -> Unit,
) : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE), OnApplyWindowInsetsListener {
    private var isAnimating = false
    private var insetView: View? = null
    private var lastWindowInsets: WindowInsetsCompat? = null

    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
        super.onPrepare(animation)
        isAnimating = true
    }

    override fun onProgress(
        insets: WindowInsetsCompat,
        runningAnimations: MutableList<WindowInsetsAnimationCompat>,
    ): WindowInsetsCompat {
        // Determine the positive difference between the ime insets and the system bar insets.
        val diff = imeMinusSystemBars(insets)
        onImeInsetAnimation(diff)
        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        super.onEnd(animation)
        if (isAnimating) {
            isAnimating = false

            // Ideally we would just call view.requestApplyInsets() and let the normal dispatch
            // cycle happen, but this happens too late resulting in a visual flicker.
            // Instead we manually dispatch the most recent WindowInsets to the view.
            lastWindowInsets?.let { lastWindowInsets ->
                insetView?.let { insetView ->
                    ViewCompat.dispatchApplyWindowInsets(insetView, lastWindowInsets)
                }
            }
        }
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        insetView = v
        lastWindowInsets = insets

        windowInsetsListener(insets)
        // If the inset application comes through and we aren't animating it, update the progress appropriately.
        if (!isAnimating) {
            onImeInsetAnimation(imeMinusSystemBars(insets))
        }

        return insets
    }

    /**
     * Determines the difference between the IME insets and the system bar insets, providing an accurate integer value
     * for the ime offset.
     */
    private fun imeMinusSystemBars(
        insets: WindowInsetsCompat,
    ): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val imeMinusSystemBars = Insets.max(
            Insets.subtract(
                insets.getInsets(WindowInsetsCompat.Type.ime()),
                insets.getInsets(WindowInsetsCompat.Type.systemBars()),
            ),
            Insets.NONE,
        )

        // Determine the positive difference between the ime insets and the system bar insets.
        imeMinusSystemBars.bottom - imeMinusSystemBars.top
    } else {
        insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()).bottom
    }
}
