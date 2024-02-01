package com.hassan.currencyconverter.feature.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hassan.currencyconverter.entity.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDown(currencyList: List<Currency>, onItemSelected: (Currency?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(Currency("", "")) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = selectedOption.name,
            onValueChange = { },
            label = { "Label" },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            currencyList.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption.name) },
                    onClick = {
                        selectedOption = selectionOption
                        expanded = false
                        onItemSelected(
                            if (selectedOption.code.isEmpty()) {
                                null
                            } else {
                                selectedOption
                            }
                        )
                    }
                )
            }
        }
    }
}