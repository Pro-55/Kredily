package com.example.kredily.data.repository.impl

import android.content.SharedPreferences
import android.content.res.AssetManager
import com.example.kredily.data.local.AppDatabase
import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Contact
import com.example.kredily.model.Employee
import com.example.kredily.model.Filter
import com.example.kredily.model.FilterOption
import com.example.kredily.model.FilterType
import com.example.kredily.model.HomeScreenState
import com.example.kredily.model.LoginStatus
import com.example.kredily.model.Resource
import com.example.kredily.model.User
import com.example.kredily.model.UserOffices
import com.example.kredily.model.local.EntityEmployee
import com.example.kredily.model.local.parse
import com.example.kredily.util.Constants
import com.example.kredily.util.extensions.readFile
import com.example.kredily.util.wrappers.resourceFlow
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class KredilyRepositoryImpl(
    private val sp: SharedPreferences,
    private val am: AssetManager,
    private val gson: Gson,
    private val db: AppDatabase,
) : KredilyRepository {

    // Global
    private val TAG = KredilyRepositoryImpl::class.java.simpleName

    override fun getLoginStatus(): Flow<Resource<LoginStatus>> = resourceFlow {
        val isLoginPending = sp.getString(Constants.KEY_USER, null) == null
        if (isLoginPending) {
            emit(Resource.Success(data = LoginStatus.LOGIN_PENDING))
            return@resourceFlow
        }

        val isPasscodePending = sp.getString(Constants.KEY_PASSCODE, null) == null
        if (isPasscodePending) {
            emit(Resource.Success(data = LoginStatus.PASSCODE_PENDING))
            return@resourceFlow
        }

        emit(Resource.Success(data = LoginStatus.LOGGED_IN))
    }

    override fun login(
        email: String,
        password: String
    ): Flow<Resource<Boolean>> = resourceFlow {
        delay(2000L) // This delay is to simulate api call

        val json = am.readFile("users.json")
        val users = gson.fromJson<List<User>>(json, object : TypeToken<List<User>>() {}.type)

        if (users.isEmpty()) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        val user = users.find { it.contact.email.equals(email, true) }
        if (user == null) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        if (user.password != password) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        sp.edit()
            .putString(Constants.KEY_USER, gson.toJson(user))
            .apply()

        emit(Resource.Success(data = true))
    }

    override fun canLoginWithOTP(email: String): Flow<Resource<Contact>> = resourceFlow {
        delay(2000L) // This delay is to simulate api call

        val json = am.readFile("users.json")
        val users = gson.fromJson<List<User>>(json, object : TypeToken<List<User>>() {}.type)

        if (users.isEmpty()) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        val user = users.find { it.contact.email.equals(email, true) }
        if (user == null) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        emit(Resource.Success(data = user.contact))
    }

    override fun verifyOTP(
        email: String,
        otp: String
    ): Flow<Resource<Boolean>> = resourceFlow {
        if (otp.isEmpty()) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        delay(2000L) // This delay is to simulate api call

        val json = am.readFile("users.json")
        val users = gson.fromJson<List<User>>(json, object : TypeToken<List<User>>() {}.type)

        if (users.isEmpty()) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        val user = users.find { it.contact.email.equals(email, true) }
        if (user == null) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        if (user.otp != otp) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        sp.edit()
            .putString(Constants.KEY_USER, gson.toJson(user))
            .apply()

        emit(Resource.Success(data = true))
    }

    override fun getOfficeLocations(): Flow<Resource<List<String>>> = resourceFlow {
        val json = am.readFile("office_locations.json")
        val userOffices =
            gson.fromJson<List<UserOffices>>(json, object : TypeToken<List<UserOffices>>() {}.type)

        delay(1500L) // This delay is to simulate api call

        if (userOffices.isEmpty()) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        val currentUser = gson.fromJson(sp.getString(Constants.KEY_USER, ""), User::class.java)

        val user = userOffices.find { it.userId == currentUser.id }
        if (user == null) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        emit(Resource.Success(data = user.offices))
    }

    override fun setPasscode(
        passcode: String?,
        confirmedPasscode: String?
    ): Flow<Resource<Boolean>> = resourceFlow {
        if (passcode.isNullOrEmpty()
            || confirmedPasscode.isNullOrEmpty()
        ) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        if (passcode != confirmedPasscode) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        sp.edit()
            .putString(Constants.KEY_PASSCODE, passcode)
            .apply()

        emit(Resource.Success(data = true))
    }


    override fun fetchEmployees(): Flow<Resource<Boolean>> = resourceFlow {
        delay(2000L) // This delay is to simulate api call

        val json = am.readFile("employees.json")
        val employees = gson.fromJson<List<EntityEmployee>>(
            json, object : TypeToken<List<EntityEmployee>>() {}.type
        )

        db.employeeDao.insertAll(employees)

        emit(Resource.Success(data = true))
    }

    override fun getEmployees(): Flow<Resource<List<Employee>>> = resourceFlow {
        val data = db.employeeDao
            .getAll()
            .parse()
        emit(Resource.Success(data = data))
    }

    override fun getHomeScreenState(
        state: HomeScreenState?,
        employees: List<Employee>
    ): Flow<Resource<HomeScreenState>> = resourceFlow {
        val safeState = state ?: HomeScreenState()

        if (employees.isEmpty()) {
            emit(Resource.Success(data = safeState))
            return@resourceFlow
        }

        val locations = mutableSetOf<String>()
        val departments = mutableSetOf<String>()
        employees.forEach { employee ->
            locations.add(employee.location)
            departments.add(employee.department)
        }

        val activeFilters = safeState.filters

        val data = safeState.copy(
            filterOptions = listOf(
                FilterOption(
                    type = FilterType.TYPE_LOCATION,
                    options = locations.toList(),
                    isActive = activeFilters.any { it.type == FilterType.TYPE_LOCATION }
                ),
                FilterOption(
                    type = FilterType.TYPE_DEPARTMENT,
                    options = departments.toList(),
                    isActive = activeFilters.any { it.type == FilterType.TYPE_DEPARTMENT }
                ),
                FilterOption(
                    type = FilterType.TYPE_OTHER,
                    options = listOf(),
                    isActive = false
                )
            ),
            employees = employees,
            filteredEmployees = getFilteredEmployees(
                employees,
                safeState.searchQuery,
                activeFilters
            )
        )

        emit(Resource.Success(data = data))
    }

    override fun searchEmployee(
        state: HomeScreenState?,
        searchQuery: String
    ): Flow<Resource<HomeScreenState>> = resourceFlow {
        if (state == null) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        val employees = state.employees
        if (employees.isEmpty()) {
            emit(Resource.Success(data = state))
            return@resourceFlow
        }

        val data = state.copy(
            searchQuery = searchQuery,
            filteredEmployees = getFilteredEmployees(employees, searchQuery, state.filters)
        )

        emit(Resource.Success(data = data))
    }

    override fun addEmployeeFilter(
        state: HomeScreenState?,
        filter: Filter
    ): Flow<Resource<HomeScreenState>> = resourceFlow {
        if (state == null) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        val employees = state.employees
        if (employees.isEmpty()) {
            emit(Resource.Success(data = state))
            return@resourceFlow
        }

        val activeFilters = state.filters
            .toMutableList()
            .apply { add(filter) }

        val data = state.copy(
            filterOptions = state.filterOptions
                .map {
                    it.copy(isActive = activeFilters.map { filter -> filter.type }
                        .contains(it.type))
                },
            filters = activeFilters,
            filteredEmployees = getFilteredEmployees(employees, state.searchQuery, activeFilters)
        )

        emit(Resource.Success(data = data))
    }

    override fun removeEmployeeFilter(
        state: HomeScreenState?,
        filter: Filter
    ): Flow<Resource<HomeScreenState>> = resourceFlow {
        if (state == null) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        val employees = state.employees
        if (employees.isEmpty()) {
            emit(Resource.Success(data = state))
            return@resourceFlow
        }

        val activeFilters = state.filters
            .toMutableList()
            .apply { remove(filter) }

        val data = state.copy(
            filterOptions = state.filterOptions
                .map {
                    it.copy(isActive = activeFilters.map { filter -> filter.type }
                        .contains(it.type))
                },
            filters = activeFilters,
            filteredEmployees = getFilteredEmployees(employees, state.searchQuery, activeFilters)
        )

        emit(Resource.Success(data = data))
    }

    override fun clearAllEmployeeFilters(
        state: HomeScreenState?
    ): Flow<Resource<HomeScreenState>> = resourceFlow {
        if (state == null) {
            emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
            return@resourceFlow
        }

        val employees = state.employees
        if (employees.isEmpty()) {
            emit(Resource.Success(data = state))
            return@resourceFlow
        }

        val activeFilters = listOf<Filter>()

        val data = state.copy(
            filterOptions = state.filterOptions
                .map { it.copy(isActive = false) },
            filters = activeFilters,
            filteredEmployees = getFilteredEmployees(
                employees,
                state.searchQuery,
                activeFilters
            )
        )

        emit(Resource.Success(data = data))
    }

    private fun getFilteredEmployees(
        employees: List<Employee>,
        searchQuery: String,
        filters: List<Filter>
    ): List<Employee> {
        val filteredBySearch = if (searchQuery.isNotEmpty()) {
            employees.filter { employee ->
                employee.firstName.contains(searchQuery, true)
                        || employee.lastName.contains(searchQuery, true)
            }
        } else employees

        val locationFilters = mutableListOf<String>()
        val departmentFilters = mutableListOf<String>()
        filters.forEach {
            val type = it.type
            if (type == FilterType.TYPE_LOCATION) {
                locationFilters.add(it.option)
            } else if (type == FilterType.TYPE_DEPARTMENT) {
                departmentFilters.add(it.option)
            }
        }

        val filteredByLocation = if (locationFilters.isNotEmpty()) {
            filteredBySearch.filter { employee ->
                locationFilters.contains(employee.location)
            }
        } else filteredBySearch

        return if (departmentFilters.isNotEmpty()) {
            filteredByLocation.filter { employee ->
                departmentFilters.contains(employee.department)
            }
        } else filteredByLocation
    }

    override fun logOut(): Flow<Resource<Boolean>> = resourceFlow {

        db.employeeDao
            .deleteAll()

        sp.edit()
            .clear()
            .apply()

        emit(Resource.Success(data = true))
    }
}