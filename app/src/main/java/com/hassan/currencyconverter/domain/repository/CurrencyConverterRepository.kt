package com.hassan.currencyconverter.domain.repository

import com.hassan.currencyconverter.domain.model.ExchangeRateWithCurrency
import com.hassan.currencyconverter.entity.ExchangeRate
import com.hassan.currencyconverter.entity.Currency
import com.hassan.currencyconverter.data.Status

interface CurrencyConverterRepository {
    suspend fun getExchangeRate(currencyCode: String): ExchangeRate?
    suspend fun getLastUpdated(): Long
    suspend fun getAllRatesWithName(): List<ExchangeRateWithCurrency>
    suspend fun insertAllExchangeRate(exchangeRates: List<ExchangeRate>)
    suspend fun insertAllCurrencies(currencies: List<Currency>)
    suspend fun getAllCurrencies(): List<Currency>
    suspend fun fetchCurrencyList(): Status<List<Currency>>
    suspend fun getExchangeRateList(): Status<List<ExchangeRate>>
}