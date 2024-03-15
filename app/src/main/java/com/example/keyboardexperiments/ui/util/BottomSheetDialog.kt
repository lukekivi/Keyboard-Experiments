package com.example.keyboardexperiments.ui.util

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import com.example.keyboardexperiments.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Sets up the dialog to be full screen regardless of content size. This includes listening to, and responding to
 * ime animations.
 *
 * This applies a small amount of space at the top of the screen, so it is not true fullscreen.
 */
fun BottomSheetDialog.setupFullscreenBottomSheet() {
    behavior.apply {
        isDraggable = false
        skipCollapsed = true
        isFitToContents = false
        state = BottomSheetBehavior.STATE_EXPANDED
    }
    findViewById<View>(com.google.android.material.R.id.coordinator)?.fitsSystemWindows = false
    findViewById<ViewGroup>(com.google.android.material.R.id.design_bottom_sheet)?.apply {
        fitsSystemWindows = false
        updateLayoutParams { height = LayoutParams.MATCH_PARENT }
    }
    // findViewById<View>(com.google.android.material.R.id.container)?.apply {
    //     fitsSystemWindows = false
    //     doOnAnimateImeInset(
    //         onImeInsetAnimation = { imeInset ->
    //             updatePadding(bottom = imeInset)
    //         },
    //         shouldConsume = true,
    //         windowInsetsListener = { _, insets, _, margin ->
    //             updateLayoutParams<MarginLayoutParams> {
    //                 updateMargins(
    //                     top = resources.getDimensionPixelSize(R.dimen.fullscreen_dialog_offset) +
    //                         insets.getInsets(Type.systemBars()).top +
    //                         margin.top,
    //                 )
    //             }
    //         }
    //     )
    // }
}