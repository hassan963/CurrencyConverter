package com.hassan.currencyconverter.data.repository

import com.hassan.currencyconverter.data.dao.ConversionDao
import com.hassan.currencyconverter.data.dao.CurrencyDao
import com.hassan.currencyconverter.di.ApiService
import com.hassan.currencyconverter.domain.model.ExchangeRateWithCurrency
import com.hassan.currencyconverter.entity.ExchangeRate
import com.hassan.currencyconverter.entity.Currency
import com.hassan.currencyconverter.data.Status
import com.hassan.currencyconverter.data.mapper.toCurrencies
import com.hassan.currencyconverter.data.mapper.toExchangeRates
import com.hassan.currencyconverter.domain.repository.CurrencyConverterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyConverterRepositoryImpl @Inject constructor(
    private val conversionRateDao: ConversionDao,
    private val currencyDao: CurrencyDao,
    private val apiService: ApiService
) : BaseRepository(), CurrencyConverterRepository {
    override suspend fun getExchangeRate(currencyCode: String): ExchangeRate? =
        withContext(Dispatchers.IO) {
            return@withContext conversionRateDao.getExchangeRate(currencyCode)
        }


    override suspend fun getLastUpdated(): Long =
        withContext(Dispatchers.IO) {
            return@withContext conversionRateDao.getLastUpdatedTime()
        }

    override suspend fun getAllRatesWithName(): List<ExchangeRateWithCurrency> =
        withContext(Dispatchers.IO) {
            return@withContext conversionRateDao.getExchangeRateWithCurrency()
        }

    override suspend fun insertAllExchangeRate(exchangeRates: List<ExchangeRate>) =
        withContext(Dispatchers.IO) {
            conversionRateDao.insertAllExchangeRate(exchangeRates)
        }

    override suspend fun insertAllCurrencies(currencies: List<Currency>) {
        withContext(Dispatchers.IO) {
            currencyDao.insertCurrencies(currencies)
        }
    }

    override suspend fun getAllCurrencies(): List<Currency> {
        return currencyDao.getAllCurrencies()
    }

    override suspend fun fetchCurrencyList(): Status<List<Currency>> {
        val response = try {
            apiCall {
                apiService.getCurrencyList()
            }
        } catch (e: Exception) {
            return Status.Error("Something went wrong")
        }

        return if (response is Status.Success && !response.data.isNullOrEmpty()) {
            Status.Success(toCurrencies(response.data))
        } else {
            Status.Error(response.message ?: "Something went wrong")
        }
    }

    override suspend fun getExchangeRateList(): Status<List<ExchangeRate>> {
        val response = try {
            apiCall {
                apiService.getLatestExchangeRates()
            }
        } catch (e: Exception) {
            return Status.Error("Something went wrong")
        }

        return if (response is Status.Success && response.data != null) {
            Status.Success(response.data.toExchangeRates())
        } else {
            Status.Error(response.message ?: "Something went wrong")
        }
    }
}