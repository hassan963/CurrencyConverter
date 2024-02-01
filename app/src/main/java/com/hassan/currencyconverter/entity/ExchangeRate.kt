package com.hassan.currencyconverter.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRate(
    @PrimaryKey
    val currencyCode: String,
    val rate: Double,
    val base: String,
    val updatedTime: Long
)