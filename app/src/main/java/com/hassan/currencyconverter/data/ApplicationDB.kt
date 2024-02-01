package com.hassan.currencyconverter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hassan.currencyconverter.data.dao.ConversionDao
import com.hassan.currencyconverter.data.dao.CurrencyDao
import com.hassan.currencyconverter.entity.ExchangeRate
import com.hassan.currencyconverter.entity.Currency

@Database(entities = [Currency::class, ExchangeRate::class], version = 1)
abstract class ApplicationDB : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun conversionRateDao(): ConversionDao
}