package com.example.codigoch.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Crea una única instancia de Retrofit para toda la aplicación. */
object RetrofitClient {

    // URL del grupo de API de Xano (debe terminar en "/"), por ejemplo:
    // "https://x8ki-letl-twmt.n7.xano.io/api:AbCdEfGh/"
    private const val URL_BASE = "https://PEGAR-AQUI-TU-INSTANCIA.xano.io/api:PEGAR-AQUI-TU-GRUPO/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
