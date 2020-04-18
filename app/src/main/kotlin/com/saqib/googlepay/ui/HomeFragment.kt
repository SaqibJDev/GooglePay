package com.saqib.googlepay.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saqib.googlepay.R
import com.saqib.googlepay.extension.disableBackNavigation

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().disableBackNavigation()

        view.findViewById<Button>(R.id.addClass).setOnClickListener {
            findNavController().navigate(R.id.action_homeToAddClass)
        }

        view.findViewById<Button>(R.id.addObject).setOnClickListener {
            findNavController().navigate(R.id.action_homeToAddObject)
        }
    }
}
