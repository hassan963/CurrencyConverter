package com.hassan.currencyconverter.feature.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hassan.currencyconverter.utils.isValidNumber

@Composable
fun CustomTextField(onTextChange: (String) -> Unit) {
    var amount by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        value = amount,
        onValueChange = {
            amount = it
            onTextChange(amount)
        },
        label = { Text(text = "Amount") },
        placeholder = { Text(text = "Enter amount") },
        trailingIcon = if (amount.isNotEmpty()) {
            {
                IconButton(onClick = { amount = "" }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }
        } else null,
        supportingText = if (amount.isNotEmpty() && !isValidNumber(amount)) {
            { Text(text = "Please enter a valid amount") }
        } else {
            null
        },
        isError = amount.isNotEmpty() && !isValidNumber(amount),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}