package com.example.kredily.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kredily.data.use_case.AddEmployeeFilterUseCase
import com.example.kredily.data.use_case.ClearAllEmployeeFiltersUseCase
import com.example.kredily.data.use_case.FetchEmployeesUseCase
import com.example.kredily.data.use_case.GetEmployeesUseCase
import com.example.kredily.data.use_case.GetHomeScreenStateUseCase
import com.example.kredily.data.use_case.RemoveEmployeeFilterUseCase
import com.example.kredily.data.use_case.SearchEmployeeUseCase
import com.example.kredily.model.Employee
import com.example.kredily.model.Filter
import com.example.kredily.model.HomeScreenState
import com.example.kredily.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchEmployeesUseCase: FetchEmployeesUseCase,
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val getHomeScreenStateUseCase: GetHomeScreenStateUseCase,
    private val searchEmployeeUseCase: SearchEmployeeUseCase,
    private val addEmployeeFilterUseCase: AddEmployeeFilterUseCase,
    private val removeEmployeeFilterUseCase: RemoveEmployeeFilterUseCase,
    private val clearAllEmployeeFiltersUseCase: ClearAllEmployeeFiltersUseCase
) : ViewModel() {

    // Global
    private val TAG = HomeViewModel::class.java.simpleName
    private val _state = MutableLiveData<Resource<HomeScreenState>>()
    val state: LiveData<Resource<HomeScreenState>> = _state

    init {
        fetchEmployees()
    }

    private fun fetchEmployees() {
        fetchEmployeesUseCase()
            .onEach {
                if (it is Resource.Success) {
                    getEmployees()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getEmployees() {
        getEmployeesUseCase()
            .onEach {
                if (it is Resource.Success) {
                    getHomeScreenState(it.data!!)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getHomeScreenState(employees: List<Employee>) {
        getHomeScreenStateUseCase(
            state = _state.value?.data,
            employees = employees
        )
            .onEach { _state.postValue(it) }
            .launchIn(viewModelScope)
    }

    fun searchEmployee(searchQuery: String) {
        searchEmployeeUseCase(
            state = _state.value?.data,
            searchQuery = searchQuery
        )
            .onEach { _state.postValue(it) }
            .launchIn(viewModelScope)
    }

    fun addEmployeeFilter(filter: Filter) {
        addEmployeeFilterUseCase(
            state = _state.value?.data,
            filter = filter
        )
            .onEach { _state.postValue(it) }
            .launchIn(viewModelScope)
    }

    fun removeEmployeeFilter(filter: Filter) {
        removeEmployeeFilterUseCase(
            state = _state.value?.data,
            filter = filter
        )
            .onEach { _state.postValue(it) }
            .launchIn(viewModelScope)
    }

    fun clearAllEmployeeFilters() {
        clearAllEmployeeFiltersUseCase(state = _state.value?.data)
            .onEach { _state.postValue(it) }
            .launchIn(viewModelScope)
    }
}