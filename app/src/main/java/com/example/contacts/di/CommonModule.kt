package com.example.contacts.di

import android.content.Context
import com.example.contacts.common.ResourceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    fun provideResourceManager(@ApplicationContext context: Context): ResourceManager =
        ResourceManager.Base(context)

}