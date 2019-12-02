package com.m.appas.data

import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * @author by M on 10/11/19
 */
interface ApiService{
    @Headers(
        "x-apikey: be21fdc6459bfb1ea42312173a92a98838cda",
        "cache-control: no-cache")
    @GET("data")
    suspend fun getAccount(): List<ApiResponse>
}