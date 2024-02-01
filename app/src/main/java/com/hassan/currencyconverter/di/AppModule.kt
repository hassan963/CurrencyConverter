package com.hassan.currencyconverter.di

import com.hassan.currencyconverter.BuildConfig
import com.hassan.currencyconverter.data.dao.ConversionDao
import com.hassan.currencyconverter.data.dao.CurrencyDao
import com.hassan.currencyconverter.data.repository.CurrencyConverterRepositoryImpl
import com.hassan.currencyconverter.domain.repository.CurrencyConverterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLoggerInterceptor() = if (BuildConfig.DEBUG) {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(appIdInterceptor: HttpInterceptor, loggingInterceptor: HttpLoggingInterceptor,): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(appIdInterceptor)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .build()
    }

    @Singleton
    @Provides
    fun provideAppIdInterceptor(): HttpInterceptor {
        return HttpInterceptor(BuildConfig.API_KEY)
    }

    @Provides
    fun providesCurrencyConverterRepository(currencyDao: CurrencyDao, conversionRateDao: ConversionDao, apiService: ApiService): CurrencyConverterRepository {
        return CurrencyConverterRepositoryImpl(conversionRateDao, currencyDao, apiService)
    }
}