package com.example.kredily.data.repository.impl

import android.content.SharedPreferences
import android.content.res.AssetManager
import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Contact
import com.example.kredily.model.LoginStatus
import com.example.kredily.model.Resource
import com.example.kredily.model.User
import com.example.kredily.model.UserOffices
import com.example.kredily.util.Constants
import com.example.kredily.util.extensions.readFile
import com.example.kredily.util.wrappers.resourceFlow
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow

class KredilyRepositoryImpl(
    private val sp: SharedPreferences,
    private val am: AssetManager,
    private val gson: Gson
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
}