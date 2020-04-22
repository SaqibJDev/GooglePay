package com.saqib.googlepay.ui.plugins.transitobject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saqib.googlepay.R
import com.saqib.googlepay.databinding.FragmentAddObjectBinding
import com.saqib.googlepay.extension.enableBackNavigation
import com.saqib.googlepay.extension.showProgressbar
import com.saqib.googlepay.gpay.model.TransitPass

class TransitObjectFragment : Fragment(), TransitObjectView {

    private var _binding: FragmentAddObjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: TransitObjectPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddObjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().enableBackNavigation()
        requireActivity().title = resources.getString(R.string.add_an_object)

        presenter = TransitObjectPresenter(requireActivity(), this)
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.trip_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.transitType.adapter = adapter
        }
    }

    private fun setupListeners() {
        binding.addObject.setOnClickListener {
            val transitPass = TransitPass(
                transitClassId = binding.classId.text.toString(),
                objectId = binding.objectId.text.toString(),
                selectedTripType = binding.transitType.selectedItem.toString(),
                origin = binding.origin.text.toString(),
                destination = binding.destination.text.toString(),
                originCode = binding.originShort.text.toString(),
                destinationCode = binding.destinationShort.text.toString(),
                passengerName = binding.passengerName.text.toString(),
                barcodeValue = binding.barcode.text.toString(),
                seatNumber = binding.seatNumber.text.toString()
            )
            presenter.onAddObject(requireContext(), transitPass)
        }

        binding.saveToGooglePay.setOnClickListener { presenter.onSaveToGPayAppClicked() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setLoading(isLoading: Boolean) {
        requireActivity().showProgressbar(isLoading)
    }

    override fun navigateBack() {
        findNavController().navigate(R.id.action_addObjectToHome)
    }

    override fun showSaveToGoogleButton(visible: Boolean) {
        binding.saveToGooglePay.visibility = if (visible) VISIBLE else GONE
    }
}
