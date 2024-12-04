package com.dokaLocal.domain.usecase

import com.dokaLocal.data.data_source.network.DokaApi
import com.dokaLocal.domain.repository.Repository
import javax.inject.Inject


class DeletePictureUseCase @Inject constructor(
    private val apiService: DokaApi
) {
    suspend fun invoke(id: String, token: String): Boolean {
        return try {
            val response = apiService.deletePicture(id, token)
            if (response.isSuccessful) {
                true
            } else {
                false
            }
        } catch (e: Exception) {
            // Handle the error and return false if failed
            false
        }
    }
}

