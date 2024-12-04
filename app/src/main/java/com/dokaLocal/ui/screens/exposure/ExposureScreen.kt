package com.dokaLocal.ui.screens.exposure

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.dokaLocal.ui.screens.timer_exposure.TimerExposureViewModel
import com.dokaLocal.ui.theme.BottomPanelColor
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.DefaultColor
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.ui.theme.RedColor
import com.dokaLocal.util.negative
import com.dokaLocal.util.rotate


@Composable
fun ExposureScreen(
    modifier: Modifier = Modifier,
    navigateExpose: () -> Unit = {},
    navigateSettings: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: ExposureViewModel = hiltViewModel(),
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MainBackgroundColor)
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
                    .padding(vertical = 60.dp),
                sharedVM = sharedVM
            )
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.6f)
                .background(MainBackgroundColor)
                .navigationBarsPadding()
        ) {
            val (bottomPanel) = createRefs()

            BottomPanel(
                modifier = Modifier
                    .constrainAs(bottomPanel) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    }
                    .navigationBarsPadding()
                    .padding(vertical = 10.dp),

                navigateExpose = navigateExpose,
                navigateSettings = navigateSettings,
                navigateBack = navigateBack,
                sharedVM = sharedVM
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
    ) {

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
                }, sharedVM = sharedVM
        )
    }
}

@Composable
fun FrameWithImage(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
    Box(modifier = modifier) {
        val image =
            remember { sharedVM.currentBitmap!!.rotate(sharedVM.savedImagesSettings.value.rotation) }

        ImageWithDimensions(image, sharedVM)
    }
}

@Composable
fun ImageWithDimensions(
    imageBitmap: Bitmap,
    sharedVM: MainViewModel,
) {
    Image(
        bitmap = imageBitmap.applyRedMultiplyEffect()!!.asImageBitmap(),
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = sharedVM.zoomState.zoom,
                scaleY = sharedVM.zoomState.zoom,
                translationX = sharedVM.zoomState.offsetX,
                translationY = sharedVM.zoomState.offsetY
            )
            .border(
                width = 3.dp,
                color = RedColor,
                shape = RoundedCornerShape(0.dp)
            ),
        contentDescription = "Image for edit",
        contentScale = ContentScale.Fit,
    )
}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    navigateExpose: () -> Unit = {},
    navigateSettings: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel()
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
            .padding(horizontal = 30.dp)
            .navigationBarsPadding()

    ) {

        Text(
            text = "Place the photo paper\nemulsion  side down\non the viewing window",
            color = RedColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .height(60.dp)
                .background(
                    color = BottomPanelColor,
//                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back_up),
                contentDescription = "Button back",
                modifier = Modifier
                    .clickable { navigateBack() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_edit),
                contentDescription = "Button settings",
                modifier = Modifier
                    .clickable { navigateSettings() }

            )
        }


        Box(
            modifier = modifier
                .height(70.dp)
                .width(241.dp)
                .background(MainBackgroundColor, shape = RoundedCornerShape(40.dp))
                .border(1.dp, color = DefaultColor, shape = RoundedCornerShape(40.dp))
                .clickable {
                    sharedVM.beforeExposure = sharedVM.currentBitmap
                    sharedVM.currentBitmap = sharedVM.currentBitmap!!.negative()
//                    sharedVM.currentBitmap = sharedVM.currentBitmap?.flippedVertically()
//                    sharedVM.currentBitmap = sharedVM.currentBitmap?.flippedHorizontally()

                    navigateExpose()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Expose",
                fontSize = 24.sp,
                color = DefaultColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }

    }
}


@Preview(showBackground = true, showSystemUi = true, name = "Exposure")
@Composable
fun GreetingPreview() {
    DOKATheme {
        ExposureScreen()
    }
}
