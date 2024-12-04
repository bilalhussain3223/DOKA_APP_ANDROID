//package com.dokaLocal.ui.screens.source_picture
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.dokaLocal.domain.usecase.GetPictureUseCase
//import com.dokaLocal.util.Resource
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.CoroutineExceptionHandler
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class ImageSourceViewModel @Inject constructor(
//    private val getPictureUseCase: GetPictureUseCase
//) : ViewModel() {
//    var state by mutableStateOf(ImageSourceScreenState())
//        private set
//
//    private val handlerException = CoroutineExceptionHandler { _, throwable ->
//        throwable.printStackTrace()
//    }
//
//    init {
//        getPicture()
//    }
//
//    private fun getPicture() {
//        viewModelScope.launch(handlerException) {
//            when (val picture = getPictureUseCase()) {
//                is Resource.Success -> {
//                    state = ImageSourceScreenState(picture = picture.data, isLoading = false)
//                }
//
//                is Resource.Error -> {
//                    state = state.copy(message = picture.message)
//                    delay(5000)
//                    getPicture()
//                }
//            }
//        }
//    }
//
//}

package com.dokaLocal.ui.screens.source_picture

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokaLocal.domain.usecase.GetPictureUseCase
import com.dokaLocal.util.Resource
import com.dokaLocal.SharedPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageSourceViewModel @Inject constructor(
    private val getPictureUseCase: GetPictureUseCase,
    private val sharedPreferencesHelper: SharedPreferencesHelper // Inject SharedPreferencesHelper here
) : ViewModel() {

    var state by mutableStateOf(ImageSourceScreenState())
        private set

    private val handlerException = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    init {
        getPicture()
    }

    private fun getPicture() {
        // Retrieve id and token from SharedPreferences using SharedPreferencesHelper
        val id = sharedPreferencesHelper.getId() ?: ""
        val token = sharedPreferencesHelper.getToken() ?: ""


        // Check if id and token are non-empty before making the API call
        if (id.isNotEmpty() && token.isNotEmpty()) {
            viewModelScope.launch(handlerException) {
                when (val picture = getPictureUseCase(id, token)) {
                    is Resource.Success -> {
                        state = ImageSourceScreenState(picture = picture.data, isLoading = false)
                    }
                    is Resource.Error -> {
                        state = state.copy(message = picture.message)
                        delay(5000)
                        getPicture() // Retry with the same id and token
                    }
                }
            }
        } else {
            state = state.copy(message = "ID or Token is missing")
        }
    }
}
