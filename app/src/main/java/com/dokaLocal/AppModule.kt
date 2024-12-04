package com.dokaLocal

import android.app.Application
import android.content.Context
import com.dokaLocal.domain.usecase.DeletePictureUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideSharedPreferencesHelper(context: Context): SharedPreferencesHelper {
        return SharedPreferencesHelper(context)
    }

    // You may also provide `Context` explicitly if needed
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

}
