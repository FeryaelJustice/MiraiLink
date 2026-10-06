package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.domain.model.affinity.*
import retrofit2.http.*

interface AffinityApiService {
    @GET("affinities") suspend fun feed(): AffinityFeed
    @GET("affinities/likes") suspend fun likes(@Query("offset") offset: Int): AffinityLikes
    @GET("affinities/requests") suspend fun requests(@Query("offset") offset: Int): AffinityRequests
    @POST("affinities/activity") suspend fun activity()
    @PUT("affinities/preferences") suspend fun participate(@Body body: Map<String, Boolean>)
    @POST("affinities/recommendations/{id}/dismiss") suspend fun dismiss(@Path("id") id: String)
    @POST("affinities/recommendations/{id}/like") suspend fun like(@Path("id") id: String): AffinityAction
    @POST("affinities/likes/{id}/return") suspend fun returnLike(@Path("id") id: String): AffinityAction
    @POST("affinities/recommendations/{id}/request") suspend fun request(@Path("id") id: String, @Body message: AffinityMessage): AffinityAction
    @POST("affinities/requests/{id}/accept") suspend fun accept(@Path("id") id: String): AffinityAction
    @POST("affinities/requests/{id}/reject") suspend fun reject(@Path("id") id: String): AffinityAction
    @POST("affinities/blocks/{id}") suspend fun block(@Path("id") peerId: String)
    @GET("affinities/contact/{id}") suspend fun contact(@Path("id") peerId: String): AffinityContact
}
