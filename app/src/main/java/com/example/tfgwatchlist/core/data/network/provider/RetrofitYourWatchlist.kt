package com.example.tfgwatchlist.core.data.network.provider

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
object RetrofitYourWatchlist {

    private const val API_BASE_URL = "https://pruebadebackend.onrender.com/"

    fun provideMoshi() = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun provideConverterFactory() = MoshiConverterFactory.create(provideMoshi())

    fun provideOkHttpClient(): OkHttpClient{
        val logging = HttpLoggingInterceptor().apply{
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Content-type", "application/json")
                .build()
            chain.proceed(request)
        }
            .addInterceptor(logging)
            .build()

    }
    fun provideRetrofit(
        client: OkHttpClient,
        converterFactory: Converter.Factory
    ) = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .client(client)
        .addConverterFactory(converterFactory)
        .build()

    fun provideRetrofit(): Retrofit =
        provideRetrofit(
            provideOkHttpClient(),
            provideConverterFactory()
        )
}