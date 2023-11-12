package com.example.kredily.ui.passcode.set_passcode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kredily.R
import com.example.kredily.databinding.LayoutOfficeLocationsAdapterItemBinding

class OfficeLocationsAdapter(
    private val data: List<String>,
    private val selectedLocation: String,
    private val listener: Listener
) : RecyclerView.Adapter<OfficeLocationsAdapter.ViewHolder>() {

    // Global
    private val TAG = OfficeLocationsAdapter::class.java.simpleName

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_office_locations_adapter_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: LayoutOfficeLocationsAdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(officeLocation: String) = with(binding) {
            txtOfficeLocation.text = officeLocation
            radioOfficeLocation.isChecked = officeLocation == selectedLocation
            root.setOnClickListener { listener.onLocationSelected(officeLocation) }
        }
    }

    interface Listener {
        fun onLocationSelected(selectedLocation: String)
    }
}