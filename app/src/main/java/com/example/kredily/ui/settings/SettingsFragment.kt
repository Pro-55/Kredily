package com.example.kredily.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.kredily.R
import com.example.kredily.databinding.FragmentSettingsBinding
import com.example.kredily.framework.BaseFragment
import com.example.kredily.model.Resource
import com.example.kredily.model.Setting
import com.example.kredily.ui.MainActivity
import com.example.kredily.util.extensions.showShortSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    //Global
    private val TAG = SettingsFragment::class.java.simpleName
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        setListeners()

        setObservers()
    }

    private fun setRecyclerView() {
        binding.recyclerSettings.adapter = SettingsAdapter(
            data = listOf(
                Setting(
                    icon = R.drawable.ic_switch_camera,
                    title = resources.getString(R.string.label_switch_camera),
                    type = Setting.Type.TYPE_SWITCH_CAMERA
                ),
                Setting(
                    icon = R.drawable.ic_key,
                    title = resources.getString(R.string.label_reset_passcode),
                    type = Setting.Type.TYPE_RESET_PASSCODE
                )
            ),
            listener = object : SettingsAdapter.Listener {
                override fun onSettingClicked(setting: Setting) {
                    showShortSnackBar(resources.getString(R.string.msg_coming_soon))
                }
            }
        )
    }

    private fun setListeners() {
        binding.imgBtnBack.setOnClickListener { onBackPressed() }

        binding.btnLogOut.setOnClickListener { viewModel.logOut() }
    }

    private fun setObservers() {
        viewModel.logOutResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> Unit
                is Resource.Success -> {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                is Resource.Error -> {
                    showShortSnackBar(resource.msg)
                }
            }
        }
    }
}