package com.example.kredily.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kredily.R
import com.example.kredily.databinding.FragmentSplashBinding
import com.example.kredily.framework.BaseFragment
import com.example.kredily.model.LoginStatus
import com.example.kredily.model.Resource
import com.example.kredily.util.extensions.updateSystemUIColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    //Global
    private val TAG = SplashFragment::class.java.simpleName
    private lateinit var binding: FragmentSplashBinding
    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.updateSystemUIColor(
            statusBarColor = R.color.colorAccent,
            navigationBarColor = R.color.colorAccent,
            isAppearanceLightStatusBars = false,
            isAppearanceLightNavigationBars = false
        )
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        viewModel.getLoginStatus()
    }

    private fun setObservers() {
        viewModel.loginStatus.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> Unit
                is Resource.Success -> {
                    val action = when (resource.data!!) {
                        LoginStatus.LOGIN_PENDING -> SplashFragmentDirections.navigateSplashToLogin()
                        LoginStatus.PASSCODE_PENDING -> SplashFragmentDirections.navigateSplashToSetPasscode()
                        LoginStatus.LOGGED_IN -> SplashFragmentDirections.navigateSplashToHome()
                    }
                    findNavController().navigate(action)
                }
                is Resource.Error -> Unit
            }
        }
    }
}