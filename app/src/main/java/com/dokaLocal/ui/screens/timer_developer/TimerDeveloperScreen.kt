package com.dokaLocal.ui.screens.timer_developer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.text.format.DateUtils
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.dokaLocal.ui.screens.timer_exposure.FrameWithImage
import com.dokaLocal.ui.screens.timer_exposure.MainFrame
import com.dokaLocal.ui.screens.timer_exposure.TimerExposureViewModel
import com.dokaLocal.ui.theme.BottomPanelColor
import com.dokaLocal.ui.theme.ButtonBackgroundColor
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.DefaultColor
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.ui.theme.RedColor
import com.dokaLocal.ui.theme.TextSimpleColor
import com.dokaLocal.ui.theme.gunplayFontFamily


@Composable
fun TimerDeveloperScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: TimerDeveloperViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    viewModel.mediaPlayer = remember { MediaPlayer.create(context, R.raw.beep_sound) }
    viewModel.navigateNext = remember { navigateNext }

    LaunchedEffect(Unit) {
        viewModel.loadProgress()
    }

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

//
//        Text(
//            modifier = Modifier
//                .constrainAs(infoText) {
//                    bottom.linkTo(bottomPanel.top, 20.dp)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                }
//                .padding(horizontal = 20.dp)
//                .alpha(0f), // Set alpha to 0 to make the text invisible
//            textAlign = TextAlign.Center,
//            text = "",
//            fontSize = 22.sp,
//            color = RedColor,
//            fontWeight = FontWeight.W500,
//            lineHeight = 30.sp,
//        )
        val (mainFrame2) = createRefs()
        MainFrame2(
            modifier = Modifier
                .constrainAs(mainFrame2) {
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
//            sharedVM = sharedVM
        )
    }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.6f)
                .navigationBarsPadding()
        ) {
            val (infoText, bottomPanel2) = createRefs()
            BottomPanel2(
                modifier = Modifier
                    .constrainAs(bottomPanel2) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                        navigateNext = navigateNext,
                navigateBack = navigateBack
            )
        }

//        Box(
//            modifier = Modifier
//                .constrainAs(bottomPanel) {
//                    bottom.linkTo(parent.bottom)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    height = Dimension.percent(0.25f) // Set height to 1/4 of the screen
//                }
//                .navigationBarsPadding()
//        ) {
//            BottomPanel(
//                navigateNext = navigateNext,
//                navigateBack = navigateBack
//            )
//        }

    }
}




@Composable
fun MainFrame2(
    modifier: Modifier = Modifier,
//    sharedVM: MainViewModel,
//    viewModel: TimerExposureViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier
            .clipToBounds()
            .navigationBarsPadding()
    ) {
//        if (viewModel.mainFrameVisible) {
//            FrameWithImage(
//                modifier = Modifier
//                    .size(
//                        width = sharedVM.imageSize?.widthDp!!,
//                        height = sharedVM.imageSize?.heightDp!!
//                    )
//                    .offset {
//                        Offset(
//                            sharedVM.savedImagesSettings.value.offsetX,
//                            sharedVM.savedImagesSettings.value.offsetY
//                        ).round()
//
//                    }, sharedVM = sharedVM
//            )
//        }
    }
}

@Composable
fun BottomPanel2(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    viewModel: TimerDeveloperViewModel = hiltViewModel()
) {
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
        )  {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back_up),
                contentDescription = "Button back",
                modifier = Modifier
                    .clickable { navigateBack() },

            )
            Text(
                modifier = Modifier.weight(1f),
                text = "Developing",
                color = TextSimpleColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Image(
                imageVector = ImageVector.vectorResource(id = if (viewModel.paused.value) R.drawable.svg_play else R.drawable.svg_pause),
                contentDescription = "Button pause",
                modifier = Modifier
                    .clickable {
                        if (viewModel.paused.value) {
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
fun GreetingPreview() {
    DOKATheme {
        TimerDeveloperScreen()
    }
}
