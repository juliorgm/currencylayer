package br.com.cuiadigital.currencylayer.data

import br.com.cuiadigital.currencylayer.data.model.Quote

interface QuotesCallback {
    fun onSucess(quotes: List<Quote>)
    fun onFailure(throwable: Throwable)
}