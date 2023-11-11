package com.example.kredily.util.wrappers

import com.example.kredily.model.Resource
import com.example.kredily.util.extensions.asResourceFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

fun <T> resourceFlow(
    doRetry: Boolean = false,
    block: suspend FlowCollector<Resource<T>>.() -> Unit
): Flow<Resource<T>> = flow(block).asResourceFlow(doRetry)