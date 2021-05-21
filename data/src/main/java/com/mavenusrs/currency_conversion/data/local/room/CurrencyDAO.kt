package com.mavenusrs.currency_conversion.data.local.room

import androidx.room.*

@Dao
interface CurrencyDAO {
    @Query("select * from currency_tbl")
    fun getAllCurrencies() : List<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetAllCurrencies(currencies : List<CurrencyEntity>)

    @Query("DELETE FROM currency_tbl")
    suspend fun deleteAll()
}