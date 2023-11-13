package com.example.kredily.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kredily.R
import com.example.kredily.databinding.LayoutFilterOptionsAdapterItemBinding
import com.example.kredily.model.FilterOption
import com.example.kredily.model.FilterType
import com.example.kredily.util.extensions.gone
import com.example.kredily.util.extensions.visible

class FilterOptionsAdapter :
    ListAdapter<FilterOption, FilterOptionsAdapter.ViewHolder>(FilterOptionDC()) {

    // Global
    private val TAG = FilterOptionsAdapter::class.java.simpleName
    private var listener: Listener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_filter_options_adapter_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    fun swapData(data: List<FilterOption>) = submitList(data.toMutableList())

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    inner class ViewHolder(
        private val binding: LayoutFilterOptionsAdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(filterOption: FilterOption) = with(binding) {
            @ColorRes val colorRes = if (filterOption.isActive) {
                R.color.colorAccent
            } else {
                R.color.color_secondary_text
            }

            val filterType = filterOption.type
            val color = ResourcesCompat.getColor(root.resources, colorRes, null)
            txtFilterOption.apply {
                text = filterType.value
                setTextColor(color)
            }

            imgDropDown.apply {
                if (filterOption.options.isNotEmpty()) {
                    visible()
                    setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
                } else gone()
            }

            layoutParentFilterOption.isSelected = filterOption.isActive

            root.setOnClickListener {
                val popupMenu = PopupMenu(binding.root.context, it)
                    .apply {
                        filterOption.options
                            .forEach { option ->
                                menu.add(option)
                            }
                    }
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    listener?.onFilterOptionSelected(
                        filterType = filterType,
                        filterOption = menuItem.toString()
                    )
                    true
                }
                popupMenu.show()
            }
        }
    }

    interface Listener {
        fun onFilterOptionSelected(
            filterType: FilterType,
            filterOption: String
        )
    }

    private class FilterOptionDC : DiffUtil.ItemCallback<FilterOption>() {
        override fun areItemsTheSame(
            oldItem: FilterOption,
            newItem: FilterOption
        ): Boolean = oldItem.type == newItem.type

        override fun areContentsTheSame(
            oldItem: FilterOption,
            newItem: FilterOption
        ): Boolean = oldItem == newItem
    }
}