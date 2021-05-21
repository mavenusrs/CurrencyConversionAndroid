package com.mavenusrs.currency_conversion.data.local.room

import androidx.room.*

@Dao
interface QuoteDAO {

    @Query("select * from quote_tbl where source LIKE :source")
    fun getAllQuotes(source: String) : List<QuoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetQuotes(quotes : List<QuoteEntity>)

    @Query("DELETE FROM quote_tbl  where source LIKE :source")
    suspend fun deleteAll(source: String)
}