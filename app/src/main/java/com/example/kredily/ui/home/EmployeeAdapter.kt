package com.example.kredily.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.kredily.R
import com.example.kredily.databinding.LayoutEmployeeAdapterItemBinding
import com.example.kredily.model.Employee
import com.example.kredily.util.extensions.addProfilePlaceholder
import com.example.kredily.util.extensions.diskCacheStrategyAll

class EmployeeAdapter(
    private val glide: RequestManager
) : ListAdapter<Employee, EmployeeAdapter.ViewHolder>(EmployeeDC()) {

    // Global
    private val TAG = EmployeeAdapter::class.java.simpleName
    private var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_employee_adapter_item,
            parent, false
        )
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) = holder.bind(getItem(position))

    fun swapData(data: List<Employee>) = submitList(data.toMutableList())

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    inner class ViewHolder(
        private val binding: LayoutEmployeeAdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee) = with(binding) {

            glide.load(employee.profileUrl)
                .diskCacheStrategyAll()
                .addProfilePlaceholder(root.context)
                .into(imgProfile)

            txtName.text = StringBuilder(employee.firstName)
                .append(" ")
                .append(employee.lastName)
                .toString()
                .trim()

            txtId.text = StringBuilder("ID:")
                .append(" ")
                .append(employee.id)

            @ColorRes val textColorRes: Int
            @ColorRes val strokeColorRes: Int
            @StringRes val textRes: Int
            if (employee.isConfigured) {
                textColorRes = R.color.color_secondary_text
                strokeColorRes = R.color.color_border
                textRes = R.string.btn_update
            } else {
                textColorRes = R.color.color_error
                strokeColorRes = R.color.color_error
                textRes = R.string.btn_configure
            }
            btnAction.apply {
                text = resources.getString(textRes)
                setTextColor(ResourcesCompat.getColor(resources, textColorRes, null))
                setStrokeColorResource(strokeColorRes)
                setOnClickListener { listener?.onConfigureUpdateEmployee(employee) }
            }

            Unit
        }
    }

    interface Listener {
        fun onConfigureUpdateEmployee(employee: Employee)
    }

    private class EmployeeDC : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(
            oldItem: Employee,
            newItem: Employee
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Employee,
            newItem: Employee
        ): Boolean = oldItem == newItem
    }
}