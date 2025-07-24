package com.pfmiranda.todoexample.di

import com.pfmiranda.todoexample.data.TodoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent // Para que Retrofit y ApiService sean singletons
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory // O MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    @Provides
    @Singleton // Asegura que solo haya una instancia de OkHttpClient
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton // Asegura que solo haya una instancia de Retrofit
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit { // OkHttpClient será inyectado por Hilt
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Usamos el OkHttpClient configurado
            .addConverterFactory(GsonConverterFactory.create()) // O MoshiConverterFactory
            .build()
    }

    @Provides
    @Singleton // Asegura que solo haya una instancia de ApiService
    fun provideApiService(retrofit: Retrofit): TodoApiService { // Retrofit será inyectado por Hilt
        return retrofit.create(TodoApiService::class.java)
    }

}