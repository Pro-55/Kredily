package com.example.kredily.ui.sign_in.login

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.navigation.fragment.findNavController
import com.example.kredily.R
import com.example.kredily.databinding.FragmentLoginBinding
import com.example.kredily.framework.BaseFragment
import com.example.kredily.model.Resource
import com.example.kredily.util.extensions.goneWithSlide
import com.example.kredily.util.extensions.shortAnimTime
import com.example.kredily.util.extensions.updateSystemUIColor
import com.example.kredily.util.extensions.visibleWithSlide
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    //Global
    private val TAG = LoginFragment::class.java.simpleName
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

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
            R.layout.fragment_login,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.postDelayed({
            binding.layoutParentLoginFields.apply {
                val parent = this.parent as ViewGroup
                visibleWithSlide(parent = parent)
            }
        }, resources.shortAnimTime)

        setListeners()

        setObservers()
    }

    private fun setListeners() {
        binding.editEmail.setOnFocusChangeListener { _, _ ->
            updateTilErrorState(binding.tilEmail, null)
        }

        binding.editPassword.setOnFocusChangeListener { _, _ ->
            updateTilErrorState(binding.tilPassword, null)
        }

        binding.btnLogIn.setOnClickListener { login() }

        binding.btnLogInWithOtp.setOnClickListener { loginWithOTP() }
    }

    private fun setObservers() {
        viewModel.loginResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> disableViews()
                is Resource.Success -> {
                    binding.layoutParentLoginFields.apply {
                        val parent = this.parent as ViewGroup
                        goneWithSlide(parent = parent)
                        postDelayed({
                            enableViews()
                            findNavController().navigate(LoginFragmentDirections.navigateLoginToSetPasscode())
                        }, resources.shortAnimTime)
                    }
                }
                is Resource.Error -> enableViews()
            }
        }

        viewModel.canLoginWithOTPResponse.observe(viewLifecycleOwner) { resource ->
            if (resource == null) return@observe

            when (resource) {
                is Resource.Loading -> disableViews()
                is Resource.Success -> {
                    viewModel.resetCanLoginWithOTPResponse()
                    enableViews()
                    findNavController().navigate(
                        LoginFragmentDirections.navigateLoginToOTPVerification(resource.data!!)
                    )
                }
                is Resource.Error -> {
                    viewModel.resetCanLoginWithOTPResponse()
                    enableViews()
                }
            }
        }
    }

    private fun login() {
        val email = binding.editEmail
            .text
            ?.toString()
            ?.trim()
            ?: ""
        val isEmailValid = isEmailValid(email)

        val password = binding.editPassword
            .text
            ?.toString()
            ?.trim()
            ?: ""
        val isPasswordValid = isPasswordValid(password)

        val emailError: String?
        val passwordError: String?
        if (isEmailValid && isPasswordValid) {
            emailError = null
            passwordError = null
            viewModel.login(email = email, password = password)
        } else {
            emailError =
                if (!isEmailValid) resources.getString(R.string.error_invalid_email) else null

            passwordError =
                if (!isPasswordValid) resources.getString(R.string.error_invalid_password) else null
        }
        updateTilErrorState(binding.tilEmail, emailError)
        updateTilErrorState(binding.tilPassword, passwordError)
    }

    private fun loginWithOTP() {
        val email = binding.editEmail
            .text
            ?.toString()
            ?.trim()
            ?: ""
        val isEmailValid = isEmailValid(email)

        val emailError = if (isEmailValid) {
            viewModel.canLoginWithOTP(email = email)
            null
        } else {
            resources.getString(R.string.error_invalid_email)
        }
        updateTilErrorState(binding.tilEmail, emailError)
    }

    private fun isEmailValid(
        email: String
    ): Boolean = email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordValid(password: String): Boolean {
        val passwordLength = password.length
        return passwordLength == 0 || passwordLength > 8
    }

    private fun updateTilErrorState(
        til: TextInputLayout,
        error: String?
    ) {
        til.error = error
    }

    private fun disableViews() {
        binding.editEmail.isEnabled = false
        binding.editPassword.isEnabled = false
        binding.btnLogIn.isEnabled = false
        binding.btnLogInWithOtp.isEnabled = false
    }

    private fun enableViews() {
        binding.editEmail.isEnabled = true
        binding.editPassword.isEnabled = true
        binding.btnLogIn.isEnabled = true
        binding.btnLogInWithOtp.isEnabled = true
    }
}