package com.tonyp.dictionary

import retrofit2.Response
import java.io.IOException

suspend inline fun <T> networkCall(crossinline block: suspend () -> Response<T>): Result<T> =
    runCatching {
        block().body() ?: throw IOException("Network error")
    }