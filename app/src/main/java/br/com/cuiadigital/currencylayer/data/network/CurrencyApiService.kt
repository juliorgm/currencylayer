package br.com.cuiadigital.currencylayer.data.network

import br.com.cuiadigital.currencylayer.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

const val API_KEY: String = BuildConfig.API_KEY
private const val BASE_URL = "http://api.currencylayer.com/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()


interface CurrencyApiService {
    @GET("live?access_key=${API_KEY}&format=1")
    fun getQuotes(): Call<ResponseQuotes>

    @GET("list?access_key=${API_KEY}&format=1")
    fun getCurrencies(): Call<ResponseCurrencies>
}

object CurrencyApi {
    val retrofitService: CurrencyApiService by lazy { retrofit.create(CurrencyApiService::class.java) }
}