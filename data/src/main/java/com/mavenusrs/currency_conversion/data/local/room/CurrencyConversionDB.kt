package com.mavenusrs.currency_conversion.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrencyEntity::class, QuoteEntity::class], version = 1, exportSchema = true)
abstract class CurrencyConversionDB : RoomDatabase() {
    abstract fun getCurrencyDAO(): CurrencyDAO
    abstract fun getQuoteDAO(): QuoteDAO
}