package com.example.keyboardexperiments.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.keyboardexperiments.R
import com.example.keyboardexperiments.ui.util.doOnAnimateImeInset
import com.example.keyboardexperiments.ui.util.setupFullscreenBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class XmlBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_KeyboardExperiments_BottomSheetDialog)
        return inflater.inflate(R.layout.fragment_xml_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireDialog() as BottomSheetDialog).setupFullscreenBottomSheet()
    }
}