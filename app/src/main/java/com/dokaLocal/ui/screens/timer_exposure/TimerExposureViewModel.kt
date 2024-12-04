package com.dokaLocal.ui.screens.timer_exposure

import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerExposureViewModel @Inject constructor() : ViewModel() {
    var mainFrameVisible by mutableStateOf(true)
    val maxTime: MutableState<Long?> = mutableStateOf(null)
    val timeLeft: MutableState<Long> = mutableStateOf(0)
    val timeSpent = mutableStateOf(0)
    val progress = mutableStateOf(1f)
    val paused = mutableStateOf(false)
    var navigateNext: () -> Unit = {}
    var mediaPlayer: MediaPlayer? = null


    private var timerJob: Job? = null
    private var soundJob: Job? = null
    private val interval = 1L

    fun loadProgress() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            maxTime.value?.let { maxTime ->
                for (i in (maxTime/1000) downTo 0 step interval) {
                    if (paused.value) return@launch
                    delay(1000)
//                    timeSpent.value += interval.toInt()
//                    timeLeft.value = (maxTime - timeSpent.value) / 1000
//                    progress.value = 1f - (timeSpent.value.toFloat() / maxTime.toFloat())
                    timeLeft.value = i
                }
                mainFrameVisible = false
                if (soundJob == null) playBeeps()
            }

        }
    }
//fun loadProgress() {
//    timerJob?.cancel()
//    timerJob = viewModelScope.launch {
//        maxTime.value = 3000 // Set maxTime to 3 seconds (3000 milliseconds)
//        maxTime.value?.let { maxTime ->
//            for (i in (maxTime / 1000) downTo 0 step interval) {
//                if (paused.value) return@launch
//                delay(1000)
//                timeLeft.value = i
//            }
//            mainFrameVisible = false
//            if (soundJob == null) playBeeps()
//        }
//    }
//}

    private fun playBeeps() {
        soundJob = viewModelScope.launch(Dispatchers.Default) {
            repeat(3) {
                async {
                    mediaPlayer?.start()
                    delay(500)
                }.await()
            }
            delay(1000)
            repeat(3) {
                async {
                    mediaPlayer?.start()
                    delay(500)
                }.await()
            }
            navigateNext()
        }
    }

    fun pauseTimer() {
        timeLeft.value?.let {
            if (it > 0) mainFrameVisible = false
            paused.value = true
        }
    }

    fun resumeTimer() {
        timeLeft.value?.let {
            if (it > 0) mainFrameVisible = true
            paused.value = false
            loadProgress()
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onCleared()
    }

}