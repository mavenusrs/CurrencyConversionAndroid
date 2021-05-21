package com.mavenusrs.currency_conversion.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
//Although it will not affect the performance when use string in this table
// ,in this case, but make it a habit to use primary key as int
@Entity(tableName = "currency_tbl")
data class CurrencyEntity (
    @PrimaryKey(autoGenerate = true )
    @ColumnInfo(name = "id")
    var id : Int,
    @ColumnInfo(name = "currency_code")
    var currencyCode : String,
    @ColumnInfo(name = "currency_name") var currencyName : String
)
