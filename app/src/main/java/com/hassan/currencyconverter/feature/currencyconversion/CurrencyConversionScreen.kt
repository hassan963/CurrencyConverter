package com.hassan.currencyconverter.feature.currencyconversion

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hassan.currencyconverter.feature.component.CurrencyCard
import com.hassan.currencyconverter.feature.component.CustomDropDown
import com.hassan.currencyconverter.feature.component.CustomTextField
import com.hassan.currencyconverter.feature.currencyconversion.viewmodel.CurrencyConversionViewModel
import com.hassan.currencyconverter.utils.showSnackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalAnimationApi
@ExperimentalCoroutinesApi
@Composable
fun CurrencyConversionScreen(viewModel: CurrencyConversionViewModel) {
    val currencyList by viewModel.currencyList.collectAsState()
    val exchangeRateList by viewModel.exchangeRateList.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val amountToConvert by viewModel.amountToConvert.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMsg.collectAsState()

    Box (
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CustomTextField { textChange ->
                viewModel.updateAmount(textChange)
            }

            Spacer(modifier = Modifier.height(12.dp))

            CustomDropDown(currencyList) { selectedCurrency ->
                viewModel.updateSelectedCurrency(selectedCurrency)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(
                    items = exchangeRateList
                ) { _, rate ->
                    if (selectedCurrency != null && amountToConvert != null) {
                        CurrencyCard(selectedCurrency!!, amountToConvert!!, rate)
                    }
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        if (exchangeRateList.isEmpty() && errorMessage.isNotEmpty()) {
            Box(modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter) {
                showSnackbar(message = errorMessage)
            }
        }
    }
}