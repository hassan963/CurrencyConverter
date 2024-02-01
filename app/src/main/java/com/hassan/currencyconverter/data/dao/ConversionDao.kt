package com.hassan.currencyconverter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hassan.currencyconverter.domain.model.ExchangeRateWithCurrency
import com.hassan.currencyconverter.entity.ExchangeRate

@Dao
interface ConversionDao {

    @Query("SELECT * FROM exchange_rates WHERE currencyCode = :currencyCode")
    fun getExchangeRate(currencyCode: String): ExchangeRate?

    @Query("SELECT MAX(updatedTime) FROM exchange_rates")
    fun getLastUpdatedTime(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExchangeRate(exchangeRates: List<ExchangeRate>)

    @Query("SELECT exchange_rates.currencyCode, exchange_rates.rate, currencies.name FROM exchange_rates JOIN currencies ON exchange_rates.currencyCode = currencies.code")
    fun getExchangeRateWithCurrency(): List<ExchangeRateWithCurrency>
}
