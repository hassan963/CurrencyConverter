package com.hassan.currencyconverter.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeRateDto(
    @SerializedName("disclaimer")
    val disclaimer: String?,
    @SerializedName("license")
    val license: String?,
    @SerializedName("timestamp")
    val timestamp: Long?,
    @SerializedName("base")
    val base: String?,
    @SerializedName("rates")
    val rates: Map<String, Double>?
)