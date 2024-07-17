package com.example.osuapp.api

import com.example.osuapp.api.friends.Friends
import com.example.osuapp.api.news.NewsData
import com.example.osuapp.api.scores.Beatmap
import com.example.osuapp.api.scores.Scores
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApi {
    @GET("me/osu")
    suspend fun getBaseData(
        @Header("Authorization") body: String,
        @Header("Content-Type") type: String = "application/json",
        ): UserData

    @GET("users/{id}/osu")
    suspend fun getFriendData(
        @Path("id") id : Int,
        @Header("Authorization") body: String,
        @Header("Content-Type") type: String = "application/json",
    ): UserData

    @GET("friends")
    suspend fun getFriends(
        @Header("Authorization") body: String,
        @Header("Content-Type") type: String = "application/json",
    ): Friends

    @GET("users/{id}/scores/{type}")
    suspend fun getUserScores(
        @Path("id") id : Int,
        @Path("type") type : String = "best",  // firsts, recent
        @Query("mode") mode : String = "osu",
        @Query("limit") limit : Int = 10,
        @Header("Authorization") body: String,
    ) : Scores


    @GET("news")
    suspend fun getNews(
        @Query("limit") limit : Int = 20, // 21 - max
        @Header("Content-Type") type: String = "application/json"
    ) : NewsData


    companion object{
        var apiService : MainApi? = null
        fun getInstance() : MainApi {
            if (apiService == null){
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

                apiService = Retrofit.Builder()
                    .baseUrl("https://osu.ppy.sh/api/v2/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(MainApi::class.java)
            }
            return apiService!!
        }
    }



}