package com.project.recipee.network

import com.project.recipee.BuildConfig.BASE_URL
import com.project.recipee.viewModel.repository.api.RecipeApis
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    @Provides
    @Singleton
    fun createNewsApiService() : RecipeApis {
        return createService().create(RecipeApis::class.java)
    }

    fun createService(): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    var client = OkHttpClient.Builder().addInterceptor(getInterceptor())
//            .addInterceptor(
//                ChuckerInterceptor.Builder(context)
//                    .collector(ChuckerCollector(context))
//                    .maxContentLength(250000L)
//                    .redactHeaders(emptySet())
//                    .alwaysReadResponseBody(false)
//                    .build()
        .addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("x-api-key", "edff5a27d2cc4585928837c7338201e5")
                .build()
            chain.proceed(newRequest)
        }.build()

    private fun getInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }
}