package com.example.kredily.util.extensions

import android.util.Log
import com.example.kredily.model.Resource
import com.example.kredily.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen

fun <T> Flow<Resource<T>>.asResourceFlow(
    doRetry: Boolean = false
): Flow<Resource<T>> = onStart { emit(Resource.Loading()) }
    .distinctUntilChanged()
    .retryWhen { cause, attempt ->
        // Exponential backoff of 1 second on each retry
        if (attempt > 1) delay(1000 * attempt)

        // Do not retry for IllegalArgument or 3 attempts are reached
        if (cause is IllegalArgumentException || attempt == 3L) false
        else doRetry
    }
    .catch {
        it.printStackTrace()
        when (it) {
            is IllegalArgumentException -> Log.d("RF", "TestLog: IllegalArgumentException")
            else -> Log.d("RF", "TestLog: Exception")
        }
        emit(Resource.Error(msg = Constants.REQUEST_FAILED_MESSAGE))
    }
    .flowOn(Dispatchers.IO)