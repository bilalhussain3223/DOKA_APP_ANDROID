package com.dokaLocal.ui.screens.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dokaLocal.SharedPreferencesHelper
import com.dokaLocal.domain.usecase.DeletePictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class EditViewModel @Inject constructor(
    private val deletePictureUseCase: DeletePictureUseCase,
    private val sharedPreferencesHelper: SharedPreferencesHelper

) : ViewModel() {

    private val _frameSize: MutableState<Size?> = mutableStateOf(null)
    val frameSize: State<Size?> = _frameSize


    private val _imageFrameSize: MutableState<Size?> = mutableStateOf(null)
    val imageFrameSize: State<Size?> = _imageFrameSize

    private val _realImageSize: MutableState<Size?> = mutableStateOf(null)
    val realImageSize: State<Size?> = _realImageSize

    private val _offset: MutableState<Offset?> = mutableStateOf(null)
    val offset: State<Offset?> = _offset

    private val _zoom = mutableFloatStateOf(1f)
    val zoom: State<Float> = _zoom

    private val _angle = mutableFloatStateOf(0f)
    val angle: State<Float> = _angle

    private val handlerException = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    init {
//        val id = sharedPreferencesHelper.getId().toString()
//        val token = sharedPreferencesHelper.getToken().toString()
//        if (id != null && token != null) {
//            deletePicture(id, token) // Pass id and token to deletePicture()
//        }

    }

    fun setFrameSize(width: Float, height: Float) {
        _frameSize.value = Size(width, height)
    }

    fun setImageFrameSize(width: Float, height: Float, widthDp: Dp, heightDp: Dp) {
        _imageFrameSize.value = Size(width, height, widthDp, heightDp)
    }

    fun setRealImageSize(bitmapWidth: Int, bitmapHeight: Int, density: Density) {
        imageFrameSize.value?.let {
            val scale = min(it.width / bitmapWidth, it.height / bitmapHeight)
            val scaledWidth = bitmapWidth * scale
            val scaledHeight = bitmapHeight * scale

            val widthDp = with(density) { scaledWidth.toDp() }
            val heightDp = with(density) { scaledHeight.toDp() }

            _realImageSize.value = Size(scaledWidth, scaledHeight, widthDp, heightDp)
        }
    }

    fun updateOffset(newOffset: Offset) {
        frameSize.value?.let { frameSizeValue ->
            realImageSize.value?.let { realImageSize ->
                _offset.value = Offset(
                    x = newOffset.x.coerceIn(0f, frameSizeValue.width - realImageSize.width),
                    y = newOffset.y.coerceIn(0f, frameSizeValue.height - realImageSize.height)
                )
            }
        }
    }


    fun updateZoom(newZoom: Float) {
        _zoom.floatValue = newZoom
    }

    fun updateAngle() {
        when (angle.value) {
            0f, 360f -> {
                _angle.floatValue = 90f
                _realImageSize.value = realImageSize.value!!.copy(
                    width = realImageSize.value!!.height,
                    height = realImageSize.value!!.width,
                    widthDp = realImageSize.value!!.heightDp,
                    heightDp = realImageSize.value!!.widthDp
                )
                updateOffset(
                    Offset(
                        offset.value!!.x + ((realImageSize.value!!.height - realImageSize.value!!.width) / 2),
                        offset.value!!.y - ((realImageSize.value!!.height - realImageSize.value!!.width) / 2)
                    )
                )
            }

            90f -> {
                _angle.floatValue = 180f
                _realImageSize.value = realImageSize.value!!.copy(
                    width = realImageSize.value!!.height,
                    height = realImageSize.value!!.width,
                    widthDp = realImageSize.value!!.heightDp,
                    heightDp = realImageSize.value!!.widthDp
                )
                updateOffset(
                    Offset(
                        offset.value!!.x - ((realImageSize.value!!.width - realImageSize.value!!.height) / 2),
                        offset.value!!.y + ((realImageSize.value!!.width - realImageSize.value!!.height) / 2)
                    )
                )
            }

            180f -> {
                _angle.floatValue = 270f
                _realImageSize.value = realImageSize.value!!.copy(
                    width = realImageSize.value!!.height,
                    height = realImageSize.value!!.width,
                    widthDp = realImageSize.value!!.heightDp,
                    heightDp = realImageSize.value!!.widthDp
                )
                updateOffset(
                    Offset(
                        offset.value!!.x + ((realImageSize.value!!.height - realImageSize.value!!.width) / 2),
                        offset.value!!.y - ((realImageSize.value!!.height - realImageSize.value!!.width) / 2)
                    )
                )
            }

            270f -> {
                _angle.floatValue = 0f
                _realImageSize.value = realImageSize.value!!.copy(
                    width = realImageSize.value!!.height,
                    height = realImageSize.value!!.width,
                    widthDp = realImageSize.value!!.heightDp,
                    heightDp = realImageSize.value!!.widthDp
                )
                updateOffset(
                    Offset(
                        offset.value!!.x - ((realImageSize.value!!.width - realImageSize.value!!.height) / 2),
                        offset.value!!.y + ((realImageSize.value!!.width - realImageSize.value!!.height) / 2)
                    )
                )
            }
        }
    }

    suspend fun deletePicture(): Result<Boolean> {
        return try {
            val id = sharedPreferencesHelper.getId().toString()
            val token = sharedPreferencesHelper.getToken().toString()
            val result = deletePictureUseCase.invoke(id, token)  // Using the UseCase here
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}

data class Size(
    val width: Float,
    val height: Float,
    val widthDp: Dp? = null,
    val heightDp: Dp? = null
)
