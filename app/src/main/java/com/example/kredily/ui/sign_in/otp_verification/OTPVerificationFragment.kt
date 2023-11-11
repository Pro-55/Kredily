package com.example.kredily.ui.sign_in.otp_verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kredily.R
import com.example.kredily.databinding.FragmentOtpVerificationBinding
import com.example.kredily.framework.BaseFragment
import com.example.kredily.model.Resource
import com.example.kredily.util.extensions.maskEmail
import com.example.kredily.util.extensions.maskPhone
import com.example.kredily.util.extensions.updateSystemUIColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OTPVerificationFragment : BaseFragment() {

    //Global
    private val TAG = OTPVerificationFragment::class.java.simpleName
    private lateinit var binding: FragmentOtpVerificationBinding
    private val args by navArgs<OTPVerificationFragmentArgs>()
    private val viewModel by viewModels<OTPVerificationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.updateSystemUIColor(
            statusBarColor = R.color.color_background,
            navigationBarColor = R.color.color_background,
            isAppearanceLightStatusBars = true,
            isAppearanceLightNavigationBars = true
        )
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_otp_verification,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()

        setListeners()

        setObservables()
    }

    private fun setView() {
        val contact = args.contact
        val email = contact.email.maskEmail()
        val mobile = StringBuilder(contact.dialCode)
            .append(contact.mobile.maskPhone())
        binding.txtPleaseEnterOtp.text =
            resources.getString(R.string.label_please_enter_otp, email, mobile)
    }

    private fun setListeners() {
        binding.imgBtnBack.setOnClickListener { onBackPressed() }

        binding.ovOtpView.setOnClickListener { updateOTPViewBorderColor(R.color.color_border) }

        binding.btnProceed.setOnClickListener {
            viewModel.verifyOTP(
                email = args.contact
                    .email,
                otp = binding.ovOtpView
                    .text
                    ?.toString()
                    ?.trim()
                    ?: ""
            )
        }
    }

    private fun setObservables() {
        viewModel.verifyOTPResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> disableViews()
                is Resource.Success -> {
                    findNavController().navigate(
                        OTPVerificationFragmentDirections.navigateOTPVerificationToPasscode()
                    )
                    enableViews()
                }
                is Resource.Error -> {
                    enableViews()
                    updateOTPViewBorderColor(R.color.color_error)
                }
            }
        }
    }

    private fun disableViews() {
        binding.imgBtnBack.isEnabled = false
        binding.ovOtpView.isEnabled = false
        binding.btnProceed.isEnabled = false
    }

    private fun enableViews() {
        binding.imgBtnBack.isEnabled = true
        binding.ovOtpView.isEnabled = true
        binding.btnProceed.isEnabled = true
    }

    private fun updateOTPViewBorderColor(@ColorRes borderColor: Int) {
        binding.ovOtpView.setLineColor(
            ResourcesCompat.getColor(resources, borderColor, null)
        )
    }
}