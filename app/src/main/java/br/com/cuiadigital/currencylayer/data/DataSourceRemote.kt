package br.com.cuiadigital.currencylayer.data

import br.com.cuiadigital.currencylayer.data.model.Currency
import br.com.cuiadigital.currencylayer.data.model.Quote
import br.com.cuiadigital.currencylayer.data.network.CurrencyApiService
import br.com.cuiadigital.currencylayer.data.network.ResponseCurrencies
import br.com.cuiadigital.currencylayer.data.network.ResponseQuotes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataSourceRemote(private val currencyApiService: CurrencyApiService) {
    fun getQuotes(callback: QuotesCallback) {
        val call = currencyApiService.getQuotes()
        call.enqueue(object : Callback<ResponseQuotes> {
            override fun onResponse(
                call: Call<ResponseQuotes>,
                response: Response<ResponseQuotes>
            ) {
                if (response.isSuccessful) {
                    val quotes = mutableListOf<Quote>()
                    response.body()?.let {
                        if (it.quotes != null)
                        for (item in it.quotes) {
                            val quote = Quote(item.key, item.value)
                            quotes.add(quote)
                        }
                    }
                    callback.onSucess(quotes)
                }
            }

            override fun onFailure(call: Call<ResponseQuotes>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }


    fun getCurrencies(callback: CurrenciesCallback) {
        val call = currencyApiService.getCurrencies()
        call.enqueue(object : Callback<ResponseCurrencies> {
            override fun onResponse(
                call: Call<ResponseCurrencies>,
                response: Response<ResponseCurrencies>
            ) {
                val listCurrencies = mutableListOf<Currency>()
                response.body()?.let {
                    if (it.currencies != null)
                    for (item in it.currencies) {
                        listCurrencies.add(Currency(item.key, item.value))
                    }
                }

                callback.onSucess(listCurrencies)
            }

            override fun onFailure(call: Call<ResponseCurrencies>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }
}