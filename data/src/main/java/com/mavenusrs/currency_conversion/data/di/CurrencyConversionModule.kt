package com.mavenusrs.currency_conversion.data.di

import com.mavenusrs.currency_conversion.data.repository.CurrencyConversionRepositoryImpl
import com.mavenusrs.domain.currency_conversion.repository.CurrencyConversionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CurrencyConversionModule {

    @Binds
    abstract fun bindCurrencyConversionRepository(
        currencyConversionRepository:
        CurrencyConversionRepositoryImpl
    ): CurrencyConversionRepository
}