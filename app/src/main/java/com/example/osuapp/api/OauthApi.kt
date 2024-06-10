package com.example.osuapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OauthApi {
    @POST("token/")
    suspend fun getApiKey(
        @Body body : Map<String,String> = mapOf(
            "client_id" to Details().cliend_id,
            "client_secret" to Details().client_secret,
            "grant_type" to "client_credentials",
            "scope" to "public"
        ),

    ) : AuthUser
    companion object{
        var apiService : OauthApi? = null
        fun getInstance() : OauthApi{
            if (apiService == null){
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

                apiService = Retrofit.Builder()
                    .baseUrl("https://osu.ppy.sh/oauth/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(OauthApi::class.java)
            }
            return apiService!!
        }
    }
}