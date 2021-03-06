package com.mavenusrs.currencyconversion.conversion_feat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mavenusrs.currencyconversion.TestCoroutineContextProvider
import com.mavenusrs.currencyconversion.TestCoroutineRule
import com.mavenusrs.currencyconversion.common.MyThrowable
import com.mavenusrs.currencyconversion.common.Resource
import com.mavenusrs.domain.currency_conversion.GetCurrenciesUseCase
import com.mavenusrs.domain.currency_conversion.GetCurrencyQuotesUseCase
import com.mavenusrs.domain.currency_conversion.model.Currency
import com.mavenusrs.domain.currency_conversion.model.Quote
import com.mavenusrs.domain.currency_conversion.model.Response
import com.mavenusrs.domain.currency_conversion.repository.CurrencyConversionRepository
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ConversionViewModelTest : TestCase() {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var currencyConversionRepository: CurrencyConversionRepository

    @Mock
    private lateinit var observer: Observer<Resource<*>>

    private lateinit var viewModel: ConversionViewModel

    @Before
    fun setup() {
        viewModel = ConversionViewModel(
            GetCurrenciesUseCase(currencyConversionRepository),
            GetCurrencyQuotesUseCase(currencyConversionRepository),
            TestCoroutineContextProvider()
        )
        viewModel.getCurrencies().observeForever(observer)
        viewModel.getQuotes().observeForever(observer)
    }

    @Test
    fun `test return items when get currencies from server return list`() =
        testCoroutineRule.runBlockTest {
            val expectedItems = listOf(Currency(1, "USD", "American Dollar"))
            // assume given
            doReturn(Response(false, expectedItems))
                .`when`(currencyConversionRepository)
                .getCurrencies()

            // call
            viewModel.fetchCurrencies()

            // verify
            val argumentCaptor = ArgumentCaptor.forClass(Resource::class.java)
            argumentCaptor.run {

                verify(observer, times(2)).onChanged(capture())

                val values = allValues
                assertEquals(Resource.Loading, values[0])
                assertEquals(
                    Resource.Success<List<*>>(expectedItems), values[1]
                )
            }

        }

    @Test
    fun `test return empty when get currencies from server return empty`() =
        testCoroutineRule.runBlockTest {

            // assume given
            doReturn(Response(false, emptyList<Currency>()))
                .`when`(currencyConversionRepository)
                .getCurrencies()

            // call
            viewModel.fetchCurrencies()

            // verify
            val argumentCaptor = ArgumentCaptor.forClass(Resource::class.java)
            argumentCaptor.run {

                verify(observer, times(2)).onChanged(capture())

                val values = allValues
                assertEquals(Resource.Loading, values[0])
                assertEquals(
                    Resource.Success<List<*>>(emptyList<Currency>()), values[1]
                )
            }

        }

    @Test
    fun `test return fresh data when get fresh currencies from server return list `() =
        testCoroutineRule.runBlockTest {
            val expectedItems = listOf(Currency(1, "USD", "American Dollar"))
            val expectedFreshness = true
            // assume given
            doReturn(Response(expectedFreshness, expectedItems))
                .`when`(currencyConversionRepository)
                .getCurrencies()

            // call
            viewModel.fetchCurrencies()

            // verify
            val argumentCaptor = ArgumentCaptor.forClass(Resource::class.java)
            argumentCaptor.run {

                verify(observer, times(2)).onChanged(capture())

                val values = allValues
                assertEquals(Resource.Loading, values[0])
                assertEquals(
                    expectedFreshness, values[1].freshData
                )
            }

        }

    @Test
    fun `test return obsolete data when get obsolete currencies from server return list `() =
        testCoroutineRule.runBlockTest {
            val expectedItems = listOf(Currency(1, "USD", "American Dollar"))
            val expectedFreshness = false
            // assume given
            doReturn(Response(expectedFreshness, expectedItems))
                .`when`(currencyConversionRepository)
                .getCurrencies()

            // call
            viewModel.fetchCurrencies()

            // verify
            val argumentCaptor = ArgumentCaptor.forClass(Resource::class.java)
            argumentCaptor.run {

                verify(observer, times(2)).onChanged(capture())

                val values = allValues
                assertEquals(Resource.Loading, values[0])
                assertEquals(
                    expectedFreshness, values[1].freshData
                )
            }

        }

    @Test
    fun `test return Failed when get currencies from server & returns error`() =
        testCoroutineRule.runBlockTest {

            // assume given
            val errorMessage = "test error message"
            val expectedErrorCode = MyThrowable.General_error_code
            val expectedThrowable = Throwable(errorMessage)

            `when`(currencyConversionRepository.getCurrencies())
                .thenAnswer { throw expectedThrowable }

            // when call
            viewModel.fetchCurrencies()

            // then verify
            val argumentCaptor = ArgumentCaptor.forClass(Resource::class.java)
            argumentCaptor.run {

                verify(observer, times(2)).onChanged(capture())

                val values = allValues
                assertEquals(Resource.Loading, values[0])
                values[1].apply {
                    assertEquals((this as Resource.Failed).myThrowable.message, errorMessage)
                    assertEquals(this.myThrowable.errorCode, expectedErrorCode)
                }
            }

        }

    //----------------test quotes --------------------------------------------------------------//

    @Test
    fun `test return items when get currency quotes from server return list`() =
        testCoroutineRule.runBlockTest {

            // assume given
            val expectedResult = listOf(Quote("USD"))
            doReturn(Response(true, expectedResult))
                .`when`(currencyConversionRepository)
                .getQuotes("USD")

            // call
            viewModel.fetchQuotes("USD", 12.0)

            // verify
            val argumentCaptor = ArgumentCaptor.forClass(Resource::class.java)
            argumentCaptor.run {

                verify(observer, times(2)).onChanged(capture())

                val values = allValues
                assertEquals(Resource.Loading, values[0])
                assertEquals(
                    Resource.Success<List<*>>(expectedResult), values[1]
                )
            }

        }


    @Test
    fun `test return empty when get currency quotes from server return 200 and empty list`() =
        testCoroutineRule.runBlockTest {

            // assume given
            val expectedResult = Response(true, emptyList<Quote>())
            doReturn(expectedResult)
                .`when`(currencyConversionRepository)
                .getQuotes("USD")

            // call
            viewModel.fetchQuotes("USD", 12.0)

            // verify
            val argumentCaptor = ArgumentCaptor.forClass(Resource::class.java)
            argumentCaptor.run {

                verify(observer, times(2)).onChanged(capture())

                val values = allValues
                assertEquals(Resource.Loading, values[0])
                assertEquals(
                    Resource.Success(emptyList<Quote>()), values[1]
                )
            }

        }

    @Test
    fun `test return Failed when get quotes from server & returns error`() =
        testCoroutineRule.runBlockTest {

            // assume given
            val errorMessage = "test error message"
            val expectedErrorCode = MyThrowable.General_error_code
            val expectedThrowable = Throwable(errorMessage)

            `when`(currencyConversionRepository.getQuotes("USD"))
                .thenAnswer { throw expectedThrowable }

            // when call
            viewModel.fetchQuotes("USD", 12.0)

            // then verify
            val argumentCaptor = ArgumentCaptor.forClass(Resource::class.java)
            argumentCaptor.run {

                verify(observer, times(2)).onChanged(capture())

                val values = allValues
                assertEquals(Resource.Loading, values[0])
                values[1].apply {
                    assertEquals((this as Resource.Failed).myThrowable.message, errorMessage)
                    assertEquals(this.myThrowable.errorCode, expectedErrorCode)
                }
            }

        }

    @Test
    fun `test return fresh items when get fresh currency quotes from server return list`() =
        testCoroutineRule.runBlockTest {

            // assume given
            val expectedResult = listOf(Quote("USD"))
            val expectedFreshNess = true
            doReturn(Response(expectedFreshNess, expectedResult))
                .`when`(currencyConversionRepository)
                .getQuotes("USD")

            // call
            viewModel.fetchQuotes("USD", 12.0)

            // verify
            val argumentCaptor = ArgumentCaptor.forClass(Resource::class.java)
            argumentCaptor.run {

                verify(observer, times(2)).onChanged(capture())

                val values = allValues
                assertEquals(Resource.Loading, values[0])
                assertEquals(
                    expectedFreshNess, values[1].freshData
                )
            }

        }

    @Test
    fun `test return obsolete items when get obsolete currency quotes from server return list`() =
        testCoroutineRule.runBlockTest {

            // assume given
            val expectedResult = listOf(Quote("USD"))
            val expectedFreshNess = false
            doReturn(Response(expectedFreshNess, expectedResult))
                .`when`(currencyConversionRepository)
                .getQuotes("USD")

            // call
            viewModel.fetchQuotes("USD", 12.0)

            // verify
            val argumentCaptor = ArgumentCaptor.forClass(Resource::class.java)
            argumentCaptor.run {

                verify(observer, times(2)).onChanged(capture())

                val values = allValues
                assertEquals(Resource.Loading, values[0])
                assertEquals(
                    expectedFreshNess, values[1].freshData
                )
            }

        }

    @After
    public override fun tearDown() {
        super.tearDown()
        //reset
        viewModel.getCurrencies().removeObserver(observer)
        viewModel.getQuotes().removeObserver(observer)
    }

}


