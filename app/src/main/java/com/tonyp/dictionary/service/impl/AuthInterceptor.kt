package com.tonyp.dictionary.service.impl

import android.content.SharedPreferences
import com.tonyp.dictionary.storage.get
import com.tonyp.dictionary.storage.models.UserPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val securePreferences: SharedPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request().newBuilder()
                .header(
                    "Authorization",
                    securePreferences
                        .get<UserPreferences>()
                        ?.getAuthHeaderValue()
                        .orEmpty()
                )
                .build())
}