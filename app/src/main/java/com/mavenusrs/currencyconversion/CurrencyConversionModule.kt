package com.mavenusrs.currencyconversion

import com.mavenusrs.currency_conversion.data.repository.CurrencyConversionRepositoryImpl
import com.mavenusrs.domain.currency_conversion.repository.CurrencyConversionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class CurrencyConversionModule {

//    @Provides
//    fun provideGetCurrenciesUseCase() : GetCurrenciesUseCase {
//        return GetCurrenciesUseCase(CurrencyConversionRepositoryImpl())
//    }
//
//    @Binds
//    abstract fun bindCurrencyConversionRepository(
//        currencyConversionRepository:
//        CurrencyConversionRepositoryImpl
//    ): CurrencyConversionRepository
}