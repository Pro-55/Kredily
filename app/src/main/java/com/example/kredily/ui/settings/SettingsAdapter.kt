package com.example.kredily.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kredily.R
import com.example.kredily.databinding.LayoutSettingsAdapterItemBinding
import com.example.kredily.model.Setting

class SettingsAdapter(
    private val data: List<Setting>,
    private val listener: Listener
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    // Global
    private val TAG = SettingsAdapter::class.java.simpleName

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_settings_adapter_item,
            parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: LayoutSettingsAdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(setting: Setting) = with(binding) {
            imgIcon.setImageResource(setting.icon)

            txtTitle.text = setting.title

            root.setOnClickListener { listener.onSettingClicked(setting) }
        }
    }

    interface Listener {
        fun onSettingClicked(setting: Setting)
    }
}