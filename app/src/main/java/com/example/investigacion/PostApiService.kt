package com.example.investigacion

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PostApiService {
    @GET("posts")
    suspend fun getUserPost(): ArrayList<PostModel>

    @POST("posts")
    suspend fun createPost(@Body newPost: PostModel): PostModel
}
