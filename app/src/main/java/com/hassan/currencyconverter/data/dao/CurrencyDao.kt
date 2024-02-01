package com.hassan.currencyconverter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hassan.currencyconverter.entity.Currency

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<Currency>)

    @Query("SELECT * FROM currencies")
    suspend fun getAllCurrencies(): List<Currency>
}