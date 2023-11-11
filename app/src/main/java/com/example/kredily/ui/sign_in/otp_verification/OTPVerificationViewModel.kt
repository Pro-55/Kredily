package com.example.kredily.ui.sign_in.otp_verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kredily.data.use_case.VerifyOTPUseCase
import com.example.kredily.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OTPVerificationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val verifyOTPUseCase: VerifyOTPUseCase
) : ViewModel() {

    // Global
    private val TAG = OTPVerificationViewModel::class.java.simpleName
    private val _verifyOTPResponse = MutableLiveData<Resource<Boolean>>()
    val verifyOTPResponse: LiveData<Resource<Boolean>> = _verifyOTPResponse

    fun verifyOTP(
        email: String,
        otp: String
    ) {
        verifyOTPUseCase(
            email = email,
            otp = otp
        )
            .onEach { _verifyOTPResponse.postValue(it) }
            .launchIn(viewModelScope)
    }
}