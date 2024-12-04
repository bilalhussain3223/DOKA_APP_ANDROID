package com.dokaLocal.ui.screens.done

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokaLocal.SharedPreferencesHelper
import com.dokaLocal.domain.repository.Repository
import com.dokaLocal.domain.usecase.DeletePictureUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class DoneViewModel  @Inject constructor (
    private val deletePictureUseCase: DeletePictureUseCase,
    private val sharedPreferencesHelper: SharedPreferencesHelper,

): ViewModel() {
//    init {
//        val id = sharedPreferencesHelper.getId()
//        val token = sharedPreferencesHelper.getToken()
//        if (id != null && token != null) {
//            deletePicture(id, token) // Pass id and token to deletePicture()
//        }
//
//    }
    private val handlerException = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
//    fun deletePicture() {
//        val id = sharedPreferencesHelper.getId().toString()
//        val token = sharedPreferencesHelper.getToken().toString()
//
//        viewModelScope.launch(handlerException) {
//            deletePictureUseCase(id, token) // Invoke the use case with the ID and Token
//        }
//    }

    }

