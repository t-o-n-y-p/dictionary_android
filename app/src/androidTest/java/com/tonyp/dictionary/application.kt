package com.tonyp.dictionary

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.runner.AndroidJUnitRunner
import com.tonyp.dictionary.service.AuthService
import com.tonyp.dictionary.service.DictionaryService
import com.tonyp.dictionary.service.impl.AuthServiceStubImpl
import com.tonyp.dictionary.service.impl.DictionaryServiceStubImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.util.Optional
import javax.inject.Singleton

@Suppress("unused") // used in gradle config
class DictionaryTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application =
        super.newApplication(cl, HiltTestApplication::class.java.name, context)

}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [com.tonyp.dictionary.Module::class]
)
class TestModule {

    @Provides
    @Singleton
    fun dictionaryService(): DictionaryService = DictionaryServiceStubImpl()

    @Provides
    @Singleton
    fun authService(@SecurePreferences securePreferences: SharedPreferences): AuthService =
        AuthServiceStubImpl(securePreferences)

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

    @Provides
    @Singleton
    @OptionalIdlingResource
    fun idlingResource(): Optional<CountingIdlingResource> =
        CountingIdlingResource("dictionary").let {
            IdlingRegistry.getInstance().register(it)
            Optional.of(it)
        }

}