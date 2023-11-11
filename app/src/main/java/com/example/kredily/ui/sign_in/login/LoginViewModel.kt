package com.example.kredily.ui.sign_in.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kredily.data.use_case.CanLoginWithOTPUseCase
import com.example.kredily.data.use_case.LoginUseCase
import com.example.kredily.model.Contact
import com.example.kredily.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase,
    private val canLoginWithOTPUseCase: CanLoginWithOTPUseCase
) : ViewModel() {

    // Global
    private val TAG = LoginViewModel::class.java.simpleName
    private val _loginResponse = MutableLiveData<Resource<Boolean>>()
    val loginResponse: LiveData<Resource<Boolean>> = _loginResponse
    private val _canLoginWithOTPResponse = MutableLiveData<Resource<Contact>?>()
    val canLoginWithOTPResponse: LiveData<Resource<Contact>?> = _canLoginWithOTPResponse

    fun login(
        email: String,
        password: String
    ) {
        loginUseCase(
            email = email,
            password = password
        )
            .onEach { _loginResponse.postValue(it) }
            .launchIn(viewModelScope)
    }

    fun canLoginWithOTP(email: String) {
        canLoginWithOTPUseCase(email = email)
            .onEach { _canLoginWithOTPResponse.postValue(it) }
            .launchIn(viewModelScope)
    }

    fun resetCanLoginWithOTPResponse() {
        _canLoginWithOTPResponse.postValue(null)
    }
}