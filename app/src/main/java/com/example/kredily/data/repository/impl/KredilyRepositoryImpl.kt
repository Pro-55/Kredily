package com.example.kredily.data.repository.impl

import android.content.SharedPreferences
import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Resource
import com.example.kredily.util.Constants
import com.example.kredily.util.wrappers.resourceFlow
import kotlinx.coroutines.flow.Flow

class KredilyRepositoryImpl(
    private val sp: SharedPreferences
) : KredilyRepository {

    // Global
    private val TAG = KredilyRepositoryImpl::class.java.simpleName

    override fun getLoginStatus(): Flow<Resource<Boolean>> = resourceFlow {
        val hasLoggedIn = sp.getBoolean(Constants.KEY_SIGN_IN_STATUS, false)

        emit(Resource.Success(data = hasLoggedIn))
    }

}