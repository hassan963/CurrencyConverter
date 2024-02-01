package com.hassan.currencyconverter.di

import com.hassan.currencyconverter.data.model.ExchangeRateDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/latest.json")
    suspend fun getLatestExchangeRates(): Response<ExchangeRateDto>

    @GET("api/currencies.json")
    suspend fun getCurrencyList(): Response<Map<String, String>>
}