package com.example.socialapp.apiservices

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://gorest.co.in/public/v2/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiServices {

    @Headers("Authorization:Bearer b9358ad700e480f8924d965da24579c89b8ea0524e669737b210ceaaa09b1a00")
    @GET("users")
    suspend fun getUsersDetails(): List<UserDetails>

    @Headers("Authorization:Bearer b9358ad700e480f8924d965da24579c89b8ea0524e669737b210ceaaa09b1a00")
    @DELETE("users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Int): Response<Unit>

    @Headers("Authorization:Bearer b9358ad700e480f8924d965da24579c89b8ea0524e669737b210ceaaa09b1a00")
    @POST("users")
    suspend fun postDataToServer(@Body post: PostData): Response<PostData>

    @Headers("Authorization:Bearer b9358ad700e480f8924d965da24579c89b8ea0524e669737b210ceaaa09b1a00")
    @PUT("users/{userId}")
    suspend fun putData(@Path("userId") userId: Int, @Body post: PostData): Response<PostData>
}

object AppApi {
    fun getRetrofitInstance(): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }
}