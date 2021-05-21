package com.mavenusrs.currency_conversion.data.remote

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CurrencyResponse (

    @SerializedName("success") var success : Boolean,
    @SerializedName("terms") var terms : String,
    @SerializedName("privacy") var privacy : String,
    @SerializedName("currencies") var currencies : JsonObject

)
