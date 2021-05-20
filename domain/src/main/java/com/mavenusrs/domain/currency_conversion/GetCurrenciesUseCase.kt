package com.mavenusrs.domain.currency_conversion

import com.mavenusrs.domain.currency_conversion.model.Currency
import com.mavenusrs.domain.currency_conversion.repository.CurrencyConversionRepository

class GetCurrenciesUseCase(private val currencyConversionRepository: CurrencyConversionRepository) {

    suspend fun getCurrencies(): List<Currency> {
        return currencyConversionRepository.getCurrencies()
    }
}