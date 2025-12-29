package com.example.galaxygram.core.di

import com.example.galaxygram.data.apod.repository.ApodRepositoryImpl
import com.example.galaxygram.domain.repository.ApodRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindApodRepository(impl: ApodRepositoryImpl): ApodRepository
}
