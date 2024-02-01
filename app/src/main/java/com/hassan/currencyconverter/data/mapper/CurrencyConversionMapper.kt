package com.hassan.currencyconverter.data.mapper

import com.hassan.currencyconverter.data.model.ExchangeRateDto
import com.hassan.currencyconverter.entity.ExchangeRate
import com.hassan.currencyconverter.entity.Currency

fun ExchangeRateDto.toExchangeRates(): List<ExchangeRate> {
    return rates?.map {
        ExchangeRate(
            currencyCode = it.key,
            rate = it.value,
            base = base ?: "",
            updatedTime = System.currentTimeMillis()
        )
    } ?: emptyList()
}

fun toCurrencies(currencies: Map<String, String>): List<Currency> {
    return currencies.map {
        Currency(
            code = it.key, name = it.value
        )
    }
}