package com.saqib.googlepay.ui.plugins.transitclass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saqib.googlepay.R
import com.saqib.googlepay.databinding.FragmentAddClassBinding
import com.saqib.googlepay.extension.enableBackNavigation
import com.saqib.googlepay.extension.showProgressbar
import com.saqib.googlepay.gpay.model.TransitPassClass

class TransitClassFragment : Fragment(), TransitClassView {

    private var _binding: FragmentAddClassBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: TransitClassPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().enableBackNavigation()
        presenter = TransitClassPresenter(this)

        binding.addClass.setOnClickListener {
            val transitPassClass = TransitPassClass(
                classId = binding.classId.text.toString(),
                issuer = binding.issuerName.text.toString(),
                selectedTransitType = binding.transitType.selectedItem.toString(),
                imageUrl = binding.logoUrl.text.toString(),
                backgroundColorCode = correctColor(binding.bgColor.text.toString())
            )
            presenter.onAddClass(requireContext(), transitPassClass)
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.transit_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.transitType.adapter = adapter
        }
    }

    private fun correctColor(color: String): String {
        var backgroundColor = color
        if (backgroundColor.isNullOrEmpty()) {
            backgroundColor = "#FFFFFF"
        }
        return if(backgroundColor[0] != '#') "#$backgroundColor" else backgroundColor
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setLoading(isLoading: Boolean) {
        requireActivity().showProgressbar(isLoading)
    }

    override fun navigateBack() {
        findNavController().navigate(R.id.action_addClassToHome)
    }
}
