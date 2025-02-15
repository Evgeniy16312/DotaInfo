package com.example.bestpractices.dev.di


import com.example.bestpractices.dev.data.api.NumberApiService
import com.example.bestpractices.dev.data.mapper.NumberFactMapper
import com.example.bestpractices.dev.data.repository.NumberRepositoryImpl
import com.example.bestpractices.dev.domain.repository.NumberRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideNumberApiService(): NumberApiService {
        return Retrofit.Builder()
            .baseUrl("http://numbersapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NumberApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNumberFactMapper(): NumberFactMapper {
        return NumberFactMapper()
    }

    @Provides
    @Singleton
    fun provideNumberRepository(
        apiService: NumberApiService,
        mapper: NumberFactMapper
    ): NumberRepository {
        return NumberRepositoryImpl(apiService, mapper)
    }
}