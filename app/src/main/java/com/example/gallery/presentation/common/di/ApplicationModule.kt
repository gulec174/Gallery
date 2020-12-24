package com.example.gallery.presentation.common.di

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val context: Context) {
    @Provides
    fun provideContentResolver(): ContentResolver {
        return context.contentResolver
    }
}