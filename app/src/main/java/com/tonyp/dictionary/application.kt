package com.tonyp.dictionary

import android.app.Application
import com.tonyp.dictionary.service.DictionaryService
import com.tonyp.dictionary.service.impl.DictionaryServiceStubImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class DictionaryApplication : Application()

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun dictionaryService(): DictionaryService = DictionaryServiceStubImpl()

}