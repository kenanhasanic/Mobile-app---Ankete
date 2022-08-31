package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAdapter {
    var apiConfig = ApiConfig()
    val retrofit : Api = Retrofit.Builder()
        .baseUrl(apiConfig.getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)
}