package com.example.myrecyclerviewpinterest.helper

import com.example.myapplication.model.Player
import com.example.myapplication.model.PlayerDatas
import com.google.gson.Gson
import io.reactivex.android.plugins.RxAndroidPlugins
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.*

interface ApiService {

    companion object{
        fun create(): ApiService{
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://demonuts.com/Demonuts/JsonTest/Tennis/")
                .build()
            return  retrofit.create(ApiService::class.java)
        }
    }

    @GET("json_parsing.php")
    fun loadDatas():Call<PlayerDatas>   // Call ornida Obserable turudi 
}