package com.luthfirr.sub1intermediate.api

import androidx.room.Entity
import com.luthfirr.sub1intermediate.api.response.AddStoryResponse
import com.luthfirr.sub1intermediate.api.response.LoginResponse
import com.luthfirr.sub1intermediate.api.response.RegisterResponse
import com.luthfirr.sub1intermediate.api.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

@Entity(tableName = "quote")
interface ApiService {
    @FormUrlEncoded
    @POST("/v1/login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password : String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("/v1/register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @Multipart
    @POST("/v1/stories")
    fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddStoryResponse>

    @GET("/v1/stories?location=1")
    fun getStories(
        @Header("Authorization") token: String
    ): Call<StoriesResponse>

    @GET("/v1/stories")
    suspend fun getStoriesPaging3(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoriesResponse

}

