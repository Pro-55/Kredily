package com.example.kredily.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kredily.R
import com.example.kredily.databinding.LayoutFiltersAdapterItemBinding
import com.example.kredily.model.Filter

class FiltersAdapter : ListAdapter<Filter, FiltersAdapter.ViewHolder>(FilterDC()) {

    // Global
    private val TAG = FiltersAdapter::class.java.simpleName
    private var listener: Listener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_filters_adapter_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    fun swapData(data: List<Filter>) = submitList(data.toMutableList())

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    inner class ViewHolder(
        private val binding: LayoutFiltersAdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(filter: Filter) = with(binding) {
            txtFilter.text = filter.option

            root.apply {
                isSelected = true
                setOnClickListener { listener?.onRemove(filter) }
            }

            Unit
        }
    }

    interface Listener {
        fun onRemove(filter: Filter)
    }

    private class FilterDC : DiffUtil.ItemCallback<Filter>() {
        override fun areItemsTheSame(
            oldItem: Filter,
            newItem: Filter
        ): Boolean = oldItem.option == newItem.option

        override fun areContentsTheSame(
            oldItem: Filter,
            newItem: Filter
        ): Boolean = oldItem == newItem
    }
}