package com.example.kredily.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.kredily.R
import com.example.kredily.databinding.FragmentHomeBinding
import com.example.kredily.framework.BaseFragment
import com.example.kredily.util.extensions.updateSystemUIColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    //Global
    private val TAG = HomeFragment::class.java.simpleName
    private lateinit var binding: FragmentHomeBinding

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
            R.layout.fragment_home,
            container, false
        )
        return binding.root
    }
}