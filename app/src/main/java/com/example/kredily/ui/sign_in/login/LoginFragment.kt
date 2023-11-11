package com.example.kredily.ui.sign_in.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.kredily.R
import com.example.kredily.databinding.FragmentLoginBinding
import com.example.kredily.framework.BaseFragment
import com.example.kredily.util.extensions.updateSystemUIColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    //Global
    private val TAG = LoginFragment::class.java.simpleName
    private lateinit var binding: FragmentLoginBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }
}