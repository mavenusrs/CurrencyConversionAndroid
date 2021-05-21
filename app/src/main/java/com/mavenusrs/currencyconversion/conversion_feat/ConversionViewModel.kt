package com.mavenusrs.currencyconversion.conversion_feat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mavenusrs.currencyconversion.common.CoroutineContextProvider
import com.mavenusrs.currencyconversion.common.MyThrowable
import com.mavenusrs.currencyconversion.common.Resource
import com.mavenusrs.domain.currency_conversion.GetCurrenciesUseCase
import com.mavenusrs.domain.currency_conversion.GetCurrencyQuotesUseCase
import com.mavenusrs.domain.currency_conversion.model.Currency
import com.mavenusrs.domain.currency_conversion.model.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getCurrencyQuotesUseCase: GetCurrencyQuotesUseCase,
    private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {

    private val currenciesLiveData = MutableLiveData<Resource<List<Currency>>>()
    private val quotesLiveData = MutableLiveData<Resource<List<Quote>>>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        currenciesLiveData.value =
            Resource.Failed(
                MyThrowable(
                    throwable.message, if (throwable is HttpException) throwable.code()
                    else MyThrowable.General_error_code
                )
            )
    }

    fun getCurrencies(): MutableLiveData<Resource<List<Currency>>> {
        return currenciesLiveData
    }

    fun fetchCurrencies() {
        currenciesLiveData.value = Resource.Loading

        viewModelScope.launch(coroutineExceptionHandler) {

            val data = withContext(coroutineContextProvider.IO) {
                getCurrenciesUseCase.getCurrencies()
            }
            currenciesLiveData.value =
                Resource.Success(data)

        }
    }

    fun getQuotes(): MutableLiveData<Resource<List<Quote>>> {
        return quotesLiveData
    }

        fun fetchQuotes(source: String, amount: Double) {
        quotesLiveData.value = Resource.Loading

        viewModelScope.launch(coroutineExceptionHandler) {

            val data = withContext(coroutineContextProvider.IO) {
                getCurrencyQuotesUseCase.getQuotes(source, amount)
            }
            quotesLiveData.value =
                Resource.Success(data)

        }
    }
}