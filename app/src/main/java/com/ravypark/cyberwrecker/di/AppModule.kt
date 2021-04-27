package com.ravypark.cyberwrecker.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.ravypark.cyberwrecker.data.ContentProviderRepository
import com.ravypark.cyberwrecker.data.ContentProviderRepositoryImpl
import com.ravypark.cyberwrecker.data.getAppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getAppPreferences()
    }

    @Singleton
    @Provides
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig
    }
}

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCpRepository(
        pref: SharedPreferences,
        config: FirebaseRemoteConfig
    ): ContentProviderRepository {
        return ContentProviderRepositoryImpl(pref, config)
    }
}

