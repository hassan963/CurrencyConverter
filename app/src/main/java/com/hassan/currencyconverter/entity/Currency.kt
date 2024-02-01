package com.hassan.currencyconverter.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class Currency(
    @PrimaryKey val code: String,
    val name: String
)