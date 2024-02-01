package com.hassan.currencyconverter.feature.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.hassan.currencyconverter.domain.model.ExchangeRateWithCurrency
import com.hassan.currencyconverter.entity.Currency
import com.hassan.currencyconverter.utils.formatToTwoDecimalPlaces

@Composable
fun CurrencyCard(
    selectedCurrency: Currency,
    amountToConvert: Double,
    rate: ExchangeRateWithCurrency
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column {
            Text(
                text = "$amountToConvert ${selectedCurrency.code} - ${formatToTwoDecimalPlaces(rate.rate)} ${rate.currencyCode}",
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    )
                    .wrapContentWidth(Alignment.Start),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            Text(
                text = "${rate.currencyCode} (${rate.name})",
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    )
                    .wrapContentWidth(Alignment.Start),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
        }
    }
}