package com.example.kredily.ui.passcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.kredily.R
import com.example.kredily.databinding.FragmentSetPasscodeBinding
import com.example.kredily.framework.BaseFragment
import com.example.kredily.util.extensions.updateSystemUIColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetPasscodeFragment : BaseFragment() {

    //Global
    private val TAG = SetPasscodeFragment::class.java.simpleName
    private lateinit var binding: FragmentSetPasscodeBinding

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
}