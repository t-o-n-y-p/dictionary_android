package com.tonyp.dictionary

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.tonyp.dictionary.service.AuthService
import com.tonyp.dictionary.service.DictionaryService
import com.tonyp.dictionary.service.impl.AuthServiceImpl
import com.tonyp.dictionary.service.impl.DictionaryServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@HiltAndroidApp
class DictionaryApplication : Application()

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun dictionaryService(@SecurePreferences securePreferences: SharedPreferences): DictionaryService =
        DictionaryServiceImpl.create(securePreferences)

    @Provides
    @Singleton
    fun authService(@SecurePreferences securePreferences: SharedPreferences): AuthService =
        AuthServiceImpl.create(securePreferences)

    @Provides
    @Singleton
    @MasterKey
    fun encryptionKey(): String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    @Provides
    @Singleton
    @SecurePreferences
    fun securePreferences(
        @ApplicationContext context: Context,
        @MasterKey key: String
    ): SharedPreferences =
        EncryptedSharedPreferences.create(
            "secureKeyValue",
            key,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    @Provides
    @Singleton
    @CommonPreferences
    fun commonPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("commonKeyValue", Context.MODE_PRIVATE)

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MasterKey

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SecurePreferences

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CommonPreferences
