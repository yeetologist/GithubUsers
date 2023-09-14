package com.github.yeetologist.githubusers.data.retrofit

import com.github.yeetologist.githubusers.data.response.DetailUserResponse
import com.github.yeetologist.githubusers.data.response.FollowUserResponseItem
import com.github.yeetologist.githubusers.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: Bearer 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")
    fun getSearch(
        @Query("q") username: String,
        @Query("page") page: Int = 1
    ): Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: Bearer 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")
    fun getDetail (
        @Path("username") username: String
    ) : Call<DetailUserResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: Bearer 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")
    fun getFollowing (
        @Path("username") username: String,
        @Query("page") page: Int = 1
    ) : Call<List<FollowUserResponseItem>>

    @GET("users/{username}/followers")
    @Headers("Authorization: Bearer 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")
    fun getFollowers (
        @Path("username") username: String,
        @Query("page") page: Int = 1
    ) : Call<List<FollowUserResponseItem>>
}