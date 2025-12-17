package com.example.tfgwatchlist.core.data.network.provider

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitHelper {
    private const val API_BASE_URL = "https://api.themoviedb.org/3/"

    private const val API_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyODZjNmRkNzRmNDk5YTg3M2RjYTIxNzk0ZmMzOTUwNiI" +
            "sIm5iZiI6MTczNzk3NTk5My4zMDU5OTk4LCJzdWIiOiI2Nzk3NjhiOTc2MGY1MWUxN2QyYjMzYWIiLCJzY29wZXMi" +
            "OlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.svV0G3rWKPjKKW2gWRyHI0Ozbv8CdiDS6NbSTQeLYNc"

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
            provideOkHttpClient(API_TOKEN),
            provideConverterFactory()
        )
}