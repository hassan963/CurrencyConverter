package com.hassan.currencyconverter.feature.currencyconversion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassan.currencyconverter.data.Status
import com.hassan.currencyconverter.domain.model.ExchangeRateWithCurrency
import com.hassan.currencyconverter.domain.repository.CurrencyConverterRepository
import com.hassan.currencyconverter.entity.Currency
import com.hassan.currencyconverter.utils.isValidNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
    private val repository: CurrencyConverterRepository
) : ViewModel() {
    private val _exchangeRateList = MutableStateFlow<List<ExchangeRateWithCurrency>>(listOf())
    val exchangeRateList get() = _exchangeRateList

    private val _currencyList = MutableStateFlow<List<Currency>>(listOf())
    val currencyList get() = _currencyList

    private val _selectedCurrency = MutableStateFlow<Currency?>(null)
    val selectedCurrency get() = _selectedCurrency

    private val _amountToConvert = MutableStateFlow<Double?>(null)
    val amountToConvert get() = _amountToConvert

    private val _errorMsg = MutableStateFlow("")
    val errorMsg get() = _errorMsg

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading

    init {
        fetchCurrencies()
        fetchExchangeRates()
    }

    fun updateSelectedCurrency(currency: Currency?) {
        _selectedCurrency.value = currency

        if (isValidNumber(_amountToConvert.value.toString())) {
            updateExchangeRates()
        } else {
            _exchangeRateList.value = emptyList()
        }
    }

    fun updateAmount(amount: String) {
        if (isValidNumber(amount)) {
            val convertedAmount = amount.toDoubleOrNull()
            _amountToConvert.value = convertedAmount
            updateExchangeRates()
        } else {
            _exchangeRateList.value = emptyList()
        }
    }

    private fun updateExchangeRates() {
        val amountToConvert = _amountToConvert.value
        val currencyCode = _selectedCurrency.value?.code

        if (amountToConvert != null && !currencyCode.isNullOrEmpty()) {
            viewModelScope.launch {
                prepareCurrencyExchangeList(amountToConvert, currencyCode)
            }
        } else {
            _exchangeRateList.value = emptyList()
        }
    }

    fun fetchCurrencies() {
        _isLoading.value = true

        viewModelScope.launch {
            _currencyList.value = repository.getAllCurrencies()

            if (_currencyList.value.isNotEmpty()) {
                _isLoading.value = false
                return@launch
            }

            when (val status = repository.fetchCurrencyList()) {
                is Status.Success -> {
                    if (!status.data.isNullOrEmpty()) {
                        _currencyList.value = status.data
                        repository.insertAllCurrencies(status.data)
                    }
                    _isLoading.value = false
                }

                is Status.Error -> {
                    _errorMsg.value = status.message ?: ""
                    _isLoading.value = false
                }

                else -> {
                    _isLoading.value = false
                }
            }
        }
    }

    fun fetchExchangeRates() {
        viewModelScope.launch {
            val lastUpdatedTime = repository.getLastUpdated()

            if (!isValidTime(lastUpdatedTime)) {
                return@launch
            }

            when (val status = repository.getExchangeRateList()) {
                is Status.Success -> {
                    if (!status.data.isNullOrEmpty()) {
                        repository.insertAllExchangeRate(status.data)
                    }
                }

                is Status.Error -> {
                    if (_exchangeRateList.value.isEmpty()) {
                        _errorMsg.value = status.message ?: ""
                    }
                }

                else -> {

                }
            }
        }
    }

    suspend fun prepareCurrencyExchangeList(amount: Double, selectedCode: String) {
        val conversionRates = repository.getAllRatesWithName()
        val currencyRate = repository.getExchangeRate(selectedCode)?.rate
        val convertedList = mutableListOf<ExchangeRateWithCurrency>()


        if (conversionRates.isNotEmpty() && amount > 0.0 && currencyRate != null && selectedCode.isNotEmpty()) {
            val toUsd = amount.div(currencyRate)

            conversionRates.forEach { item ->
                if (item.currencyCode != selectedCode) convertedList.add(
                    ExchangeRateWithCurrency(
                        item.currencyCode, item.rate.times(toUsd), item.name
                    )
                )
            }

            _exchangeRateList.value = convertedList.sortedBy { it.currencyCode }
        } else {
            _exchangeRateList.value = emptyList()
            _errorMsg.value = "No rate is available for the selected currency"
        }
    }

    fun isValidTime(lastUpdated: Long): Boolean {
        return lastUpdated + 30 * 60000 < System.currentTimeMillis()
    }
}