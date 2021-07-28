package br.com.cuiadigital.currencylayer.util

import br.com.cuiadigital.currencylayer.data.model.Quote
import java.util.*

class CurrencyConversions {
    companion object{

        fun convertCurrency(listQuotes : List<Quote>, originCode: String, destinyCode:String, conversionNumber: Double): Double {
            val valueQuoteOrigin = codeToDolarQuote(listQuotes, originCode)
            val valueQuoteDesity = codeToDolarQuote(listQuotes, destinyCode)

            val dolar: Double = convertToDolar(valueQuoteOrigin, conversionNumber)
            val valueConverted = valueQuoteDesity * dolar * conversionNumber

            return valueConverted
        }

        private fun convertToDolar(valueQuoteOrigin: Double, conversionNumber: Double): Double {
            if (valueQuoteOrigin >= DOLAR) return DOLAR / valueQuoteOrigin
            return valueQuoteOrigin / DOLAR
        }

        fun codeToDolarQuote(listQuotes: List<Quote>, code: String): Double {
            val value = listQuotes.filter{ it.code == "USD${code}" }
            return value[0].value
        }

        fun getSymbol(currencyCode: String): String {
            return try {
                val currency = Currency.getInstance(currencyCode)
                currency.symbol
            } catch (e: Exception) {
                currencyCode
            }
        }

        const val DOLAR = 1.0
    }
}