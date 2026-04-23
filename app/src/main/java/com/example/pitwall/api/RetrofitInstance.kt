package com.example.pitwall.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance { //object je kotlin singleton - existuje presne jedna instancia v celej apke
    private const val BASE_URL = "https://api.jolpi.ca/ergast/f1/"

    // by lazy inicializacia sa odlozi na moment ked sa api prvykrat pouzije,
    //Retrofit sa nevytvori pri starte apky ale az ked ho skutocne potrebuje, uspora zdrojov
    val api: F1ApiService by lazy {
        Retrofit.Builder() //navrhovy vzor nastavim parametre a na konci zavolam build
            .baseUrl(BASE_URL) //vsetky api volania budu relativne k tejto url
            .addConverterFactory(GsonConverterFactory.create()) //json odpovede prevadza pomocou gson
            .build()
            .create(F1ApiService::class.java) //dynamicky implementuje rozhranie F1apiservice
    }
}