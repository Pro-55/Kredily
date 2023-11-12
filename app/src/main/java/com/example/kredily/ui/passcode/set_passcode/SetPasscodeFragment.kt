package com.example.kredily.ui.passcode.set_passcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kredily.R
import com.example.kredily.databinding.FragmentSetPasscodeBinding
import com.example.kredily.framework.BaseFragment
import com.example.kredily.model.Resource
import com.example.kredily.util.extensions.updateSystemUIColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetPasscodeFragment : BaseFragment() {

    //Global
    private val TAG = SetPasscodeFragment::class.java.simpleName
    private lateinit var binding: FragmentSetPasscodeBinding
    private val viewModel by viewModels<SetPasscodeViewModel>()
    private val officeLocations = mutableListOf<String>()
    private var selectedLocation: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.updateSystemUIColor(
            statusBarColor = R.color.colorAccent,
            navigationBarColor = R.color.color_background,
            isAppearanceLightStatusBars = false,
            isAppearanceLightNavigationBars = true
        )
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_set_passcode, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        setObservers()
    }

    private fun setListeners() {
        binding.txtOfficeLocation.setOnClickListener { showOfficeLocationsSheet() }

        binding.btnProceed.setOnClickListener {
            viewModel.setPasscode(
                passcode = binding.editEnterPasscode
                    .text
                    ?.toString()
                    ?.trim(),
                confirmedPasscode = binding.editConfirmPasscode
                    .text
                    ?.toString()
                    ?.trim()
            )
        }
    }

    private fun setObservers() {
        viewModel.officeLocations.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> disableViews()
                is Resource.Success -> {
                    officeLocations.clear()
                    officeLocations.addAll(resource.data!!)
                    updateSelectedLocation(officeLocations[0])
                    enableViews()
                }
                is Resource.Error -> enableViews()
            }
        }

        viewModel.setPasscodeResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> disableViews()
                is Resource.Success -> {
                    enableViews()
                    findNavController().navigate(SetPasscodeFragmentDirections.navigateSetPasscodeToHome())
                }
                is Resource.Error -> enableViews()
            }
        }
    }

    private fun showOfficeLocationsSheet() {
        OfficeLocationsSheetFragment.showDialog(
            officeLocations = officeLocations,
            selectedLocation = selectedLocation,
            listener = object : OfficeLocationsSheetFragment.Listener {
                override fun onLocationSelected(selectedLocation: String) {
                    updateSelectedLocation(selectedLocation)
                }
            },
            fm = childFragmentManager,
            tag = TAG
        )
    }

    private fun updateSelectedLocation(officeLocation: String) {
        selectedLocation = officeLocation
        binding.txtOfficeLocation.text = selectedLocation
    }

    private fun disableViews() {
        binding.editEnterPasscode.isEnabled = false
        binding.editConfirmPasscode.isEnabled = false
        binding.txtOfficeLocation.isEnabled = false
        binding.btnProceed.isEnabled = false
    }

    private fun enableViews() {
        binding.editEnterPasscode.isEnabled = true
        binding.editConfirmPasscode.isEnabled = true
        binding.txtOfficeLocation.isEnabled = true
        binding.btnProceed.isEnabled = true
    }
}