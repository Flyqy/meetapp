package com.example.meetapp.di.network

import com.example.meetapp.BuildConfig
import com.example.meetapp.network.MarvelService
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.sql.Timestamp
import java.util.concurrent.TimeUnit

object Network {

    private val retrofitClient = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
        .create(MarvelService::class.java)

    val networkModule = module {
        single { retrofitClient }
    }

    private val okHttpClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)

            builder.addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()

                val timeStamp = Timestamp(System.currentTimeMillis()).time.toString()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("ts", timeStamp)
                    .addQueryParameter("apikey", BuildConfig.PUBLIC_KEY)
                    .addQueryParameter(
                        "hash",
                        hashString(
                            timeStamp +
                                    BuildConfig.PRIVATE_KEY +
                                    BuildConfig.PUBLIC_KEY,
                            "MD5"
                        )
                    )
                    .build()

                val requestBuilder = original.newBuilder().url(url)

                chain.proceed(requestBuilder.build())
            }

            return builder.build()
        }

    private fun hashString(input: String, algorithm: String): String = MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}