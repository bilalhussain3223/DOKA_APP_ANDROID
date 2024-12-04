package com.dokaLocal.ui.screens.timer_exposure

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.text.format.DateUtils
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.dokaLocal.MainViewModel
import com.dokaLocal.R
import com.dokaLocal.ui.screens.edit.applyRedMultiplyEffect
import com.dokaLocal.ui.screens.exposure.ImageWithDimensions
import com.dokaLocal.ui.theme.BottomPanelColor
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.ui.theme.RedColor
import com.dokaLocal.ui.theme.TextSimpleColor
import com.dokaLocal.ui.theme.gunplayFontFamily
import com.dokaLocal.util.rotate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun TimerExposureScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: TimerExposureViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.timeLeft.value = sharedVM.timeForExposure.floatValue.toLong()
        viewModel.maxTime.value = sharedVM.timeForExposure.floatValue.toLong() * 1000L
        delay(1000)

        viewModel.loadProgress()
    }
    viewModel.mediaPlayer = remember { MediaPlayer.create(context, R.raw.beep_sound) }
    viewModel.navigateNext = remember { navigateNext }

    BackHandler {
        navigateBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackgroundColor)

    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .navigationBarsPadding()
        ) {
            val (mainFrame) = createRefs()
            MainFrame(
                modifier = Modifier
                    .constrainAs(mainFrame) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 30.dp)
                    .navigationBarsPadding()
                    .padding(vertical = 60.dp),
                sharedVM = sharedVM
            )
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.6f)
                .navigationBarsPadding()
        ) {
            val (bottomPanel) = createRefs()

            BottomPanel(
                modifier = Modifier
                    .constrainAs(bottomPanel) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                navigateNext = navigateNext,
                navigateBack = navigateBack
            )
        }
    }

}

@Composable
fun MainFrame(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: TimerExposureViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier
            .clipToBounds()
            .navigationBarsPadding()
    ) {
        if (viewModel.mainFrameVisible) {
            FrameWithImage(
                modifier = Modifier
                    .size(
                        width = sharedVM.imageSize?.widthDp!!,
                        height = sharedVM.imageSize?.heightDp!!
                    )
                    .offset {
                        Offset(
                            sharedVM.savedImagesSettings.value.offsetX,
                            sharedVM.savedImagesSettings.value.offsetY
                        ).round()
                    },
                sharedVM = sharedVM
            )
        }
    }
}

//
//@Composable
//fun FrameWithImage(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
//    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//
//    // Load bitmap in a coroutine
//    LaunchedEffect(sharedVM.currentBitmap) {
//        launch {
//            val bitmap = withContext(Dispatchers.IO) {
//                // Ensure the bitmap is rotated as required
//                sharedVM.currentBitmap?.rotate(sharedVM.savedImagesSettings.value.rotation)
//            }
//            imageBitmap = bitmap
//            isLoading = false
//        }
//    }
//
//    Box(modifier = modifier) {
//        imageBitmap?.let { bitmap ->
//            Image(
//                bitmap = bitmap.asImageBitmap(),
//                modifier = Modifier
//                    .fillMaxSize()
//                    .graphicsLayer(
//                        scaleX = -sharedVM.zoomState.zoom,
//                        scaleY = sharedVM.zoomState.zoom,
//                        translationX = -sharedVM.zoomState.offsetX,
//                        translationY = sharedVM.zoomState.offsetY
//                    ),
//                contentDescription = "Image for edit",
//                contentScale = ContentScale.Fit
//            )
//        } ?: run {
//            if (isLoading) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
//        }
//    }
//}

@Composable
fun FrameWithImage(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
    val imageBitmap = sharedVM.currentBitmap!!.rotate(sharedVM.savedImagesSettings.value.rotation) // Get the current bitmap directly

    Box(modifier = modifier) {
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = sharedVM.zoomState.zoom,
                        scaleY = sharedVM.zoomState.zoom,
                        translationX = sharedVM.zoomState.offsetX,
                        translationY = sharedVM.zoomState.offsetY
                    ),
                contentDescription = "Image for edit",
                contentScale = ContentScale.Fit
            )
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
//@Composable
//fun FrameWithImage(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
//    Box(modifier = modifier) {
//        val image =
//            remember { sharedVM.currentBitmap!!.rotate(sharedVM.savedImagesSettings.value.rotation) }
//
//        ImageWithDimensions(image, sharedVM)
//    }
//}



@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    val viewModel: TimerExposureViewModel = hiltViewModel()
    val isPaused by viewModel.paused

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = BottomPanelColor,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(vertical = 16.dp, horizontal = 30.dp)
            .navigationBarsPadding()

    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .padding(bottom = 20.dp)
                .height(60.dp)
                .background(
                    color = BottomPanelColor,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back_up),
                contentDescription = "Button back",
                modifier = Modifier
                    .clickable { navigateBack() }
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "Exposing",
                color = TextSimpleColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Image(
                imageVector = ImageVector.vectorResource(id = if (isPaused) R.drawable.svg_play else R.drawable.svg_pause),
                contentDescription = "Button pause",
                modifier = Modifier
                    .clickable {
                        if (isPaused) {
                            viewModel.resumeTimer()
                        } else {
                            viewModel.pauseTimer()
                        }
                    }

            )
        }

        Text(
            text = DateUtils.formatElapsedTime(viewModel.timeLeft.value),
            color = TextSimpleColor,
            fontSize = 60.sp,
            textAlign = TextAlign.Center,
            fontFamily = gunplayFontFamily,
            modifier = Modifier
                .padding(bottom = 40.dp)
        )
    }
}

@SuppressLint("DefaultLocale")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TimerExposurePreview() {
    DOKATheme {
        TimerExposureScreen()
    }
}
