package com.hassan.currencyconverter.domain.model

data class ExchangeRateWithCurrency(
    val currencyCode: String,
    val rate: Double,
    val name: String
)