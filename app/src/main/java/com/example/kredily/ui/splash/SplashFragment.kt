package com.example.kredily.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_splash,
            container, false
        )
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
                    val action: NavDirections
                    val extras: FragmentNavigator.Extras
                    when (resource.data!!) {
                        LoginStatus.LOGIN_PENDING -> {
                            action = SplashFragmentDirections.navigateSplashToLogin()
                            extras = FragmentNavigatorExtras(
                                binding.imgIcon to binding.imgIcon.transitionName
                            )
                        }
                        LoginStatus.PASSCODE_PENDING -> {
                            action = SplashFragmentDirections.navigateSplashToSetPasscode()
                            extras = FragmentNavigatorExtras(
                                binding.imgIcon to binding.imgIcon.transitionName
                            )
                        }
                        LoginStatus.LOGGED_IN -> {
                            action = SplashFragmentDirections.navigateSplashToHome()
                            extras = FragmentNavigatorExtras()
                        }
                    }
                    findNavController().navigate(action, extras)
                }
                is Resource.Error -> Unit
            }
        }
    }
}