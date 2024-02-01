package com.hassan.currencyconverter.navigation

sealed class Screens(val route: String) {
    object currencyConverterScreen: Screens("mainScreen")
}