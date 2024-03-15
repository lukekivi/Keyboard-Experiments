package com.example.keyboardexperiments.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.keyboardexperiments.R
import com.example.keyboardexperiments.ui.util.doOnAnimateImeInset

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.home_button_open_xml_bottom_sheet).setOnClickListener {
            findNavController().navigate(R.id.nav_graph_xml_bottom_sheet)
        }
        view.findViewById<Button>(R.id.home_button_open_compose_bottom_sheet).setOnClickListener {
            findNavController().navigate(R.id.nav_graph_compose_bottom_sheet)
        }
        view.findViewById<Button>(R.id.home_button_open_nav_graph_bottom_sheet).setOnClickListener {
            findNavController().navigate(R.id.nav_graph_bottom_sheet_nav_graph)
        }
        view.findViewById<View>(R.id.home_container).apply {
            doOnAnimateImeInset(
                shouldConsume = false,
                onImeInsetAnimation = { imeInset ->
                    updatePadding(bottom = imeInset)
                },
                windowInsetsListener = { _, insets, _, margin ->
                    updateLayoutParams<MarginLayoutParams> {
                        updateMargins(
                            bottom = insets.getInsets(Type.systemBars()).bottom + margin.bottom,
                        )
                    }
                },
            )
        }
    }
}