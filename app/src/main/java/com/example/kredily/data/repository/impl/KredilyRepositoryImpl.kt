package com.example.kredily.data.repository.impl

import android.content.SharedPreferences
import android.content.res.AssetManager
import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Contact
import com.example.kredily.model.Resource
import com.example.kredily.model.User
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

    override fun getLoginStatus(): Flow<Resource<Boolean>> = resourceFlow {
        val hasLoggedIn = sp.getString(Constants.KEY_USER, null) != null

        emit(Resource.Success(data = hasLoggedIn))
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
}