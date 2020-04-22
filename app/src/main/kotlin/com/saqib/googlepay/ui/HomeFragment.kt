package com.saqib.googlepay.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saqib.googlepay.R
import com.saqib.googlepay.databinding.FragmentHomeBinding
import com.saqib.googlepay.extension.disableBackNavigation

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().disableBackNavigation()

        binding.addClass.setOnClickListener {
            findNavController().navigate(R.id.action_homeToAddClass)
        }

        binding.addObject.setOnClickListener {
            findNavController().navigate(R.id.action_homeToAddObject)
        }
    }
}
