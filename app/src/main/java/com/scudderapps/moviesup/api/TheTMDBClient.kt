package com.scudderapps.moviesup.api

import com.scudderapps.moviesup.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY: String = BuildConfig.API_KEY
const val BASE_URL: String = "https://api.themoviedb.org/3/"
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original"
const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20
const val TRAILER_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/"
const val TRAILER_THUMBNAIL_END_URL = "/0.jpg"


object TheTMDBClient {
    fun getClient(): TmdbApiInterface {

        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter(
                    "api_key",
                    API_KEY
                )
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApiInterface::class.java)
    }

}