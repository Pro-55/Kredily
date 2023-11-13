package com.example.kredily.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kredily.R
import com.example.kredily.databinding.FragmentHomeBinding
import com.example.kredily.framework.BaseFragment
import com.example.kredily.model.Employee
import com.example.kredily.model.Filter
import com.example.kredily.model.FilterOption
import com.example.kredily.model.FilterType
import com.example.kredily.model.Resource
import com.example.kredily.util.extensions.glide
import com.example.kredily.util.extensions.gone
import com.example.kredily.util.extensions.goneWithFade
import com.example.kredily.util.extensions.hideKeyboard
import com.example.kredily.util.extensions.setOnButtonCheckedListener
import com.example.kredily.util.extensions.setTextChangedListener
import com.example.kredily.util.extensions.showShortSnackBar
import com.example.kredily.util.extensions.updateSystemUIColor
import com.example.kredily.util.extensions.visible
import com.example.kredily.util.extensions.visibleWithFade
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    //Global
    private val TAG = HomeFragment::class.java.simpleName
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val glide by lazy { glide() }
    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) = Unit

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val searchQuery = char?.toString()?.trim() ?: ""
            viewModel.searchEmployee(searchQuery = searchQuery)
        }
    }
    private val employees = mutableListOf<Employee>()
    private val unconfiguredEmployees = mutableListOf<Employee>()
    private var tabIndex = 0
    private var filterOptionsAdapter: FilterOptionsAdapter? = null
    private var filtersAdapter: FiltersAdapter? = null
    private var employeeAdapter: EmployeeAdapter? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFilterOptionsRecyclerView()

        setupFiltersRecyclerView()

        setupEmployeeRecyclerView()

        setListeners()

        setObservers()
    }

    private fun setupFilterOptionsRecyclerView() {
        filterOptionsAdapter = FilterOptionsAdapter()
        filterOptionsAdapter?.setListener(object : FilterOptionsAdapter.Listener {
            override fun onFilterOptionSelected(
                filterType: FilterType,
                filterOption: String
            ) {
                viewModel.addEmployeeFilter(
                    filter = Filter(
                        type = filterType,
                        option = filterOption
                    )
                )
            }
        })
        binding.recyclerFilterOptions.adapter = filterOptionsAdapter
    }

    private fun setupFiltersRecyclerView() {
        filtersAdapter = FiltersAdapter()
        filtersAdapter?.setListener(object : FiltersAdapter.Listener {
            override fun onRemove(filter: Filter) {
                viewModel.removeEmployeeFilter(filter)
            }
        })
        binding.recyclerFilters.adapter = filtersAdapter
    }

    private fun setupEmployeeRecyclerView() {
        employeeAdapter = EmployeeAdapter(glide)
        employeeAdapter?.setListener(object : EmployeeAdapter.Listener {
            override fun onConfigureUpdateEmployee(employee: Employee) {
                showShortSnackBar(resources.getString(R.string.msg_coming_soon))
            }
        })
        binding.recyclerEmployee.adapter = employeeAdapter
    }

    private fun setListeners() {
        binding.editSearchEmployee.setTextChangedListener(watcher)

        binding.imgBtnSettings.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.navigateHomeToSettings()
            )
        }

        binding.mtbToggleGroup.setOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@setOnButtonCheckedListener

            tabIndex = when (checkedId) {
                binding.btnTotal.id -> 0
                binding.btnUnconfigured.id -> 1
                else -> return@setOnButtonCheckedListener
            }
            setEmployees()
        }

        binding.txtBtnClearAll.setOnClickListener { viewModel.clearAllEmployeeFilters() }

        binding.fabCamera.setOnClickListener { showShortSnackBar(resources.getString(R.string.msg_coming_soon)) }
    }

    private fun setObservers() {
        viewModel.state.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    hideError()
                    hideFAB()
                    showLoader()
                }
                is Resource.Success -> {
                    val data = resource.data!!

                    setFilterOptions(data.filterOptions)
                    setFilters(data.filters)

                    employees.clear()
                    employees.addAll(data.filteredEmployees)
                    unconfiguredEmployees.clear()
                    unconfiguredEmployees.addAll(employees.filter { !it.isConfigured })
                    setTabView()
                    setEmployees()

                    hideError()
                    hideLoader()
                    showFAB()
                }
                is Resource.Error -> {
                    clearFocus()
                    hideLoader()
                    hideFAB()
                    showError()
                    showShortSnackBar(resource.msg)
                }
            }
        }
    }

    private fun setFilterOptions(filterOptions: List<FilterOption>) {
        if (filterOptions.isNotEmpty()) {
            binding.recyclerFilterOptions.visible()
            filterOptionsAdapter?.swapData(filterOptions)
        } else {
            binding.recyclerFilterOptions.gone()
        }
    }

    private fun setFilters(filters: List<Filter>) {
        if (filters.isNotEmpty()) {
            binding.recyclerFilters.visible()
            filtersAdapter?.swapData(filters)
            binding.txtBtnClearAll.apply {
                val parent = this.parent as ViewGroup
                visibleWithFade(parent)
            }
        } else {
            binding.txtBtnClearAll.apply {
                val parent = this.parent as ViewGroup
                goneWithFade(parent)
            }
            binding.recyclerFilters.gone()
        }
    }

    private fun setTabView() {
        binding.btnTotal.text = resources.getString(R.string.label_total, employees.size)
        binding.btnUnconfigured.text =
            resources.getString(R.string.label_unconfigured, unconfiguredEmployees.size)
    }

    private fun setEmployees() {
        val data = if (tabIndex == 0) employees else unconfiguredEmployees
        employeeAdapter?.swapData(data)
    }

    private fun showLoader() {
        binding.parentLayoutLoader.visible()
    }

    private fun hideLoader() {
        binding.parentLayoutLoader.gone()
    }

    private fun showError() {
        binding.parentLayoutError.visible()
    }

    private fun hideError() {
        binding.parentLayoutError.gone()
    }

    private fun showFAB() {
        binding.fabCamera.apply {
            val parent = this.parent as ViewGroup
            visibleWithFade(parent)
        }
    }

    private fun hideFAB() {
        binding.fabCamera.apply {
            val parent = this.parent as ViewGroup
            goneWithFade(parent)
        }
    }

    private fun clearFocus() {
        requireActivity().currentFocus?.clearFocus()
        hideKeyboard()
    }

    private fun hideKeyboard() {
        requireActivity().hideKeyboard()
    }
}