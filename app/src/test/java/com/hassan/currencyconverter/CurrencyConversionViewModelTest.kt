package com.hassan.currencyconverter

import com.hassan.currencyconverter.data.Status
import com.google.common.truth.Truth.assertThat
import com.hassan.currencyconverter.entity.Currency
import com.hassan.currencyconverter.feature.currencyconversion.viewmodel.CurrencyConversionViewModel
import io.mockk.coEvery
import io.mockk.mockk
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hassan.currencyconverter.domain.model.ExchangeRateWithCurrency
import com.hassan.currencyconverter.domain.repository.CurrencyConverterRepository
import com.hassan.currencyconverter.entity.ExchangeRate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyConversionViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var repository: CurrencyConverterRepository
    private lateinit var viewModel: CurrencyConversionViewModel

    @Before
    fun setup() {
        repository = mockk()
        viewModel = CurrencyConversionViewModel(repository)
    }

    @Test
    fun `fetchCurrencies should update currencyList on success`() =
        testDispatcher.runBlockingTest {
            val currencies = listOf(Currency("USD", "US Dollar"))
            coEvery { repository.getAllCurrencies() } returns emptyList()
            coEvery { repository.fetchCurrencyList() } returns Status.Success(currencies)

            viewModel.fetchCurrencies()

            assertThat(viewModel.currencyList.value).isNotEmpty()
        }

    @Test
    fun `fetchCurrencies should update errorMsg on error`() =
        testDispatcher.runBlockingTest {
            coEvery { repository.getAllCurrencies() } returns emptyList()
            coEvery { repository.fetchCurrencyList() } returns Status.Error("Error")

            viewModel.fetchCurrencies()

            assertThat(viewModel.errorMsg.value).isEqualTo("Error")
        }

    @Test
    fun `fetchExchangeRates should update errorMsg on error`() =
        testDispatcher.runBlockingTest {
            coEvery { repository.getLastUpdated() } returns System.currentTimeMillis() - 40 * 60000
            coEvery { repository.getExchangeRateList() } returns Status.Error("Error")

            viewModel.fetchExchangeRates()

            assertThat(viewModel.errorMsg.value).isEqualTo("Error")
        }

    @Test
    fun `when last updated is less than 30 mins ago, should return a false`() =
        testDispatcher.runBlockingTest {
            val isValidTime = viewModel.isValidTime(System.currentTimeMillis() - 15 * 60000)

            assertThat(isValidTime).isFalse()
        }

    @Test
    fun `when last updated is more than 30 mins ago, should return a true`() =
        testDispatcher.runBlockingTest {
            val isValidTime = viewModel.isValidTime(System.currentTimeMillis() - 40 * 60000)

            assertThat(isValidTime).isTrue()
        }

    @Test
    fun `prepareCurrencyExchangeList should update exchangeRateList on success`() =
        testDispatcher.runBlockingTest {
            val amount = 100.0
            val selectedCode = "EUR"
            val conversionRates = listOf(
                ExchangeRateWithCurrency("EUR", 1.5, "Euro"),
                ExchangeRateWithCurrency("USD", 1.2, "US Dollar"),
                ExchangeRateWithCurrency("GBP", 0.9, "British Pound"),
            )
            val currencyRate = 1.5

            coEvery { repository.getAllRatesWithName() } returns conversionRates
            coEvery { repository.getExchangeRate(selectedCode) } returns ExchangeRate(
                selectedCode,
                currencyRate,
                base = "USD",
                updatedTime = 2323
            )

            viewModel.prepareCurrencyExchangeList(amount, selectedCode)

            assertThat(viewModel.exchangeRateList.value).isNotEmpty()
            assertThat(viewModel.errorMsg.value).isEmpty()
            assertThat(viewModel.exchangeRateList.value).containsExactly(
                ExchangeRateWithCurrency("GBP", 60.00000000000001, "British Pound"),
                ExchangeRateWithCurrency("USD", 80.0, "US Dollar")
            ).inOrder()
        }

    @Test
    fun `prepareCurrencyExchangeList should update errorMsg when no rate is available`() =
        testDispatcher.runBlockingTest {
            val amount = 100.0
            val selectedCode = "EUR"

            coEvery { repository.getAllRatesWithName() } returns emptyList()
            coEvery { repository.getExchangeRate(selectedCode) } returns null

            viewModel.prepareCurrencyExchangeList(amount, selectedCode)

            assertThat(viewModel.errorMsg.value).isEqualTo("No rate is available for the selected currency")
            assertThat(viewModel.exchangeRateList.value).isEmpty()
        }

    @Test
    fun `when amount is invalid, should return empty list`() = testDispatcher.runBlockingTest {
        val amount = 0.0
        val selectedCode = "EUR"
        val conversionRates = listOf(
            ExchangeRateWithCurrency("EUR", 1.5, "Euro"),
            ExchangeRateWithCurrency("USD", 1.2, "US Dollar"),
            ExchangeRateWithCurrency("GBP", 0.9, "British Pound"),
        )
        val currencyRate = 1.5

        coEvery { repository.getAllRatesWithName() } returns conversionRates
        coEvery { repository.getExchangeRate(selectedCode) } returns ExchangeRate(
            selectedCode,
            currencyRate,
            base = "USD",
            updatedTime = 2323
        )

        viewModel.prepareCurrencyExchangeList(amount, selectedCode)

        assertThat(viewModel.exchangeRateList.value).isEmpty()
    }

    @Test
    fun `when amount is valid but currency code is not selected, should return empty list`() =
        testDispatcher.runBlockingTest {
            val amount = 100.0
            val selectedCode = ""
            val conversionRates = listOf(
                ExchangeRateWithCurrency("EUR", 1.5, "Euro"),
                ExchangeRateWithCurrency("USD", 1.2, "US Dollar"),
                ExchangeRateWithCurrency("GBP", 0.9, "British Pound"),
            )
            val currencyRate = 1.5

            coEvery { repository.getAllRatesWithName() } returns conversionRates
            coEvery { repository.getExchangeRate(selectedCode) } returns ExchangeRate(
                selectedCode,
                currencyRate,
                base = "USD",
                updatedTime = 2323
            )

            viewModel.prepareCurrencyExchangeList(amount, selectedCode)

            assertThat(viewModel.exchangeRateList.value).isEmpty()
        }

}