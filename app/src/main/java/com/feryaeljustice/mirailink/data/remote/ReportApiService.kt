package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.request.report.ReportUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApiService {
    @POST("report")
    suspend fun reportUser(@Body request: ReportUserRequest): Response<Unit>
}