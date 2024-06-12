package com.example.osuapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("users/{user_id}/osu")
    suspend fun getBaseData(
        @Header("Authorization") body: String,
        @Path("user_id") userId : String =  "",
        @Header("Content-Type") type: String = "application/json",
        @Query("key") key : String = "officiis",
        ): UserData
    companion object{
        var apiService : UserApi? = null
        fun getInstance() : UserApi {
            if (apiService == null){
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

                apiService = Retrofit.Builder()
                    .baseUrl("https://osu.ppy.sh/api/v2/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(UserApi::class.java)
            }
            return apiService!!
        }
    }
}