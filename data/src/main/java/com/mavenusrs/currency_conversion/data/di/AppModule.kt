package com.mavenusrs.currency_conversion.data.di

import android.content.Context
import androidx.room.Room
import com.mavenusrs.currency_conversion.data.BuildConfig
import com.mavenusrs.currency_conversion.data.local.room.CurrencyConversionDB
import com.mavenusrs.currency_conversion.data.local.room.CurrencyDAO
import com.mavenusrs.currency_conversion.data.local.room.QuoteDAO
import com.mavenusrs.currency_conversion.data.remote.CurrencyConversionAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideCurrencyConversionDB(@ApplicationContext context : Context) = Room.databaseBuilder(
        context,
        CurrencyConversionDB::class.java,
        "currency_db"
    ).build()

    @Provides
    fun provideCurrencyDAO(currencyConversionDB: CurrencyConversionDB): CurrencyDAO {
        return currencyConversionDB.getCurrencyDAO()
    }

    @Provides
    fun provideQuoteDAO(currencyConversionDB: CurrencyConversionDB): QuoteDAO {
        return currencyConversionDB.getQuoteDAO()
    }

    @Singleton
    @Provides
    fun provideCurrencyConversionAPI(): CurrencyConversionAPI {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .build()

        return retrofit.create(CurrencyConversionAPI::class.java)
    }
}