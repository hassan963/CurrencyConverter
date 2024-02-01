package com.hassan.currencyconverter.utils

import java.text.DecimalFormat

fun formatToTwoDecimalPlaces(value: Double): String {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(value)
}

fun isValidNumber(input: String): Boolean {
    val regex = Regex("^(0|[1-9]\\d*)(\\.\\d+)?$")
    return regex.matches(input)
}
