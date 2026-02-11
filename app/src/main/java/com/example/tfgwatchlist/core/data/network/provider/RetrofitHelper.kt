package com.example.tfgwatchlist.core.data.network.provider

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.tfgwatchlist.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitHelper {
    private const val API_BASE_URL = "https://api.themoviedb.org/3/"

    fun provideMoshi() = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun provideConverterFactory() = MoshiConverterFactory.create(provideMoshi())

    fun provideOkHttpClient(token: String) = OkHttpClient.Builder()
        .addInterceptor {
            it.proceed(
                it.request().newBuilder()
                    .addHeader("accept", "application.json")
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            )
        }
        .build()
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
            provideOkHttpClient(BuildConfig.API_TOKEN),
            provideConverterFactory()
        )
}