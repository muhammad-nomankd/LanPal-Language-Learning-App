package com.example.langpal.di

import com.example.langpal.data.repository.VocabularyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryDomain {
    @Singleton
    @Provides
    fun provideRepository(): VocabularyRepository{
        return VocabularyRepository()
    }
}