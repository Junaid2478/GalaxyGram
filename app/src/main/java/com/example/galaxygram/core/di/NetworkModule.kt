package com.example.galaxygram.core.di

import com.example.galaxygram.data.apod.NasaApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor { msg ->
            android.util.Log.d("HTTP", msg.replace(Regex("(api_key=)[^&\\s]+"), "$1███"))
        }.apply { level = HttpLoggingInterceptor.Level.BASIC }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())  
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideNasaApi(retrofit: Retrofit): NasaApi =
        retrofit.create(NasaApi::class.java)
}
