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