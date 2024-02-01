package com.hassan.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.hassan.currencyconverter.data.ApplicationDB
import com.hassan.currencyconverter.data.dao.ConversionDao
import com.hassan.currencyconverter.data.dao.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule{

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): ApplicationDB {
        return Room.databaseBuilder(
            appContext,
            ApplicationDB::class.java,
            "currency_converter_db"
        ).build()
    }

    @Provides
    fun provideCurrencyDao(appDatabase: ApplicationDB): CurrencyDao {
        return appDatabase.currencyDao()
    }

    @Provides
    fun providesConversionRateDao(appDatabase: ApplicationDB): ConversionDao {
        return appDatabase.conversionRateDao()
    }
}
