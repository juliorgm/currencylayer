package br.com.cuiadigital.currencylayer.data.network

data class ResponseCurrencies (var currencies : Map<String, String>)

data class ResponseQuotes( val quotes:  Map<String, Double>)