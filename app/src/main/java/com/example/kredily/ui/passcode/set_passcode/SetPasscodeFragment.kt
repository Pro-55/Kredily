package com.example.kredily.ui.passcode.set_passcode

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.navigation.fragment.findNavController
import com.example.kredily.R
import com.example.kredily.databinding.FragmentSetPasscodeBinding
import com.example.kredily.framework.BaseFragment
import com.example.kredily.model.Resource
import com.example.kredily.util.Constants
import com.example.kredily.util.extensions.goneWithSlide
import com.example.kredily.util.extensions.shortAnimTime
import com.example.kredily.util.extensions.showShortSnackBar
import com.example.kredily.util.extensions.updateSystemUIColor
import com.example.kredily.util.extensions.visibleWithSlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetPasscodeFragment : BaseFragment() {

    //Global
    private val TAG = SetPasscodeFragment::class.java.simpleName
    private lateinit var binding: FragmentSetPasscodeBinding
    private val viewModel by viewModels<SetPasscodeViewModel>()
    private val officeLocations = mutableListOf<String>()
    private var selectedLocation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionSet().apply {
            addTransition(ChangeTransform())
            addTransition(ChangeBounds())
            interpolator = FastOutLinearInInterpolator()
        }
    }

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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_set_passcode,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.postDelayed({
            binding.layoutParentPasscodeFields.apply {
                val parent = this.parent as ViewGroup
                visibleWithSlide(parent = parent)
            }
        }, resources.shortAnimTime)

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
                    enableViews()
                    officeLocations.clear()
                    officeLocations.addAll(resource.data!!)
                    if (officeLocations.isEmpty()) {
                        showShortSnackBar(Constants.REQUEST_FAILED_MESSAGE)
                        return@observe
                    }
                    updateSelectedLocation(officeLocations[0])
                }
                is Resource.Error -> {
                    enableViews()
                    showShortSnackBar(resource.msg)
                }
            }
        }

        viewModel.setPasscodeResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> disableViews()
                is Resource.Success -> {
                    binding.layoutParentPasscodeFields.apply {
                        val parent = this.parent as ViewGroup
                        goneWithSlide(parent = parent)
                        postDelayed({
                            enableViews()
                            findNavController().navigate(SetPasscodeFragmentDirections.navigateSetPasscodeToHome())
                        }, resources.shortAnimTime)
                    }
                }
                is Resource.Error -> {
                    enableViews()
                    showShortSnackBar(resource.msg)
                }
            }
        }
    }

    private fun showOfficeLocationsSheet() {
        if (officeLocations.isEmpty()) {
            showShortSnackBar(resources.getString(R.string.error_invalid_data))
            return
        }
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