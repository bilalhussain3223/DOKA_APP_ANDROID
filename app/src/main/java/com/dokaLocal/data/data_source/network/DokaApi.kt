package com.dokaLocal.data.data_source.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//interface DokaApi {
//
//    @GET("1?token=eiA2QEhl9a9wyjYJsT2gjxrlUA9L2DuY")
//    suspend fun getPicture(): Response<ResponseBody>
//
//    @DELETE("1?token=eiA2QEhl9a9wyjYJsT2gjxrlUA9L2DuY")
//    suspend fun deletePicture(): Response<ResponseBody>
//}
interface DokaApi {

    @GET("image/{id}")
    suspend fun getPicture(
        @Path("id") id: String,           // Dynamic `id` in the URL path
        @Query("token") token: String     // Query parameter for `token`
    ): Response<ResponseBody>

    @DELETE("image/{id}")
    suspend fun deletePicture(
        @Path("id") id: String,           // Dynamic `id` in the URL path
        @Query("token") token: String     // Query parameter for `token`
    ): Response<ResponseBody>
}