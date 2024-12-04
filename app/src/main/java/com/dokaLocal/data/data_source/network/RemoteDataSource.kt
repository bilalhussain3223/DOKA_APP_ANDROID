package com.dokaLocal.data.data_source.network


import dagger.Provides
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

//class RemoteDataSource @Inject constructor(private val api: DokaApi) {
//
//    suspend fun getPicture(id: String, token: String): Response<ResponseBody> {
//        return api.getPicture(id, token)
//    }
//
//    suspend fun deletePicture(id: String, token: String): Response<ResponseBody> {
//        return api.deletePicture(id, token)
//    }
//}

class RemoteDataSource @Inject constructor(private val api: DokaApi) {

    suspend fun getPicture(id: String, token: String): Response<ResponseBody> {
        return api.getPicture(id, token) // Calling the getPicture API with query params
    }

    suspend fun deletePicture(id: String, token: String): Response<ResponseBody> {
        return api.deletePicture(id, token) // Corrected: using 'api' instead of 'apiService'
    }

}