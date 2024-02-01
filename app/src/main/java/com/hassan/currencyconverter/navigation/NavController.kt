package com.hassan.currencyconverter.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hassan.currencyconverter.feature.currencyconversion.CurrencyConversionScreen
import com.hassan.currencyconverter.feature.currencyconversion.viewmodel.CurrencyConversionViewModel

@Composable
fun setupNavController(controller: NavHostController, startDestination: String) {
    NavHost(navController = controller, startDestination = startDestination) {
        composable(route = Screens.currencyConverterScreen.route) {
            val viewModel = hiltViewModel<CurrencyConversionViewModel>()
            CurrencyConversionScreen(viewModel = viewModel)
        }
    }
}