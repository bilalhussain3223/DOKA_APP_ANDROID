package com.dokaLocal.ui.screens.settings.saturation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
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
import com.dokaLocal.ui.theme.BottomPanelColor
import com.dokaLocal.ui.theme.ButtonBackgroundColor
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.ui.theme.TextSimpleColor
import com.dokaLocal.util.changeBitmapSaturationOld
import com.dokaLocal.util.loadCompressedBitmap
import com.dokaLocal.util.rotate

@Composable
fun SaturationScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: SaturationViewModel = hiltViewModel()
) {
    viewModel.saturation.floatValue = sharedVM.saturation.floatValue

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackgroundColor)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)

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
                    .padding(vertical = 10.dp),
                sharedVM = sharedVM,
                navigateNext = navigateNext,
                navigateBack = navigateBack
            )
        }
    }
}

@Composable
fun MainFrame(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
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
        sharedVM.currentBitmap?.let {
            Image(
                bitmap = it.rotate(sharedVM.savedImagesSettings.value.rotation).asImageBitmap(),
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
        }
    }
}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel,
    viewModel: SaturationViewModel = hiltViewModel()
) {
    var saturationDefault = remember { sharedVM.saturation.floatValue }
    sharedVM.currentBitmap = sharedVM.currentBitmap?.let { loadCompressedBitmap(it) }
    sharedVM.changedBitmap = remember { sharedVM.currentBitmap }

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

    ) {
        Row(
            modifier = modifier
                .padding(bottom = 20.dp)
                .height(60.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back_up),
                contentDescription = "Button back",
                modifier = Modifier
                    .clickable {
                        sharedVM.currentBitmap = sharedVM.changedBitmap
                        sharedVM.saturation.floatValue = saturationDefault
                        navigateBack()
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "Saturation",
                color = TextSimpleColor,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_check),
                contentDescription = "Button Next",
                modifier = Modifier
                    .clickable {
                        sharedVM.changedBitmap = sharedVM.currentBitmap
                        saturationDefault = viewModel.saturation.floatValue
                        sharedVM.saturation.floatValue = viewModel.saturation.floatValue
                        navigateNext()
                    }
            )
        }
        SaturationSlider(modifier, sharedVM)
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaturationSlider(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: SaturationViewModel = hiltViewModel()
) {

    val colors = SliderDefaults.colors(
        thumbColor = ButtonBackgroundColor,
        activeTrackColor = TextSimpleColor,
        inactiveTrackColor = TextSimpleColor,
    )
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = 40.dp)
    ) {
        Image(
            modifier = Modifier.clickable {
                if (viewModel.saturation.floatValue > 0) {
                    viewModel.saturation.floatValue -= 0.01f
                    changeBitmap(viewModel, sharedVM)
                }
            },
            imageVector = ImageVector.vectorResource(id = R.drawable.svg_minus),
            contentDescription = "Minus"
        )
        Slider(
            modifier = Modifier.weight(1f),
            track = { sliderState ->
                SliderDefaults.Track(
                    modifier = Modifier
                        .scale(scaleX = 1f, scaleY = 2f),
                    sliderState = sliderState, colors = colors
                )
            },
            thumb = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(ButtonBackgroundColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%.2f", viewModel.saturation.floatValue),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainBackgroundColor
                    )
                }
            },
            valueRange = 0f..2f,
            value = viewModel.saturation.floatValue,
            onValueChange = {
                viewModel.saturation.floatValue = it
                changeBitmap(viewModel, sharedVM)
            }
        )

        Image(
            modifier = Modifier.clickable {
                if (viewModel.saturation.floatValue < 2) {
                    viewModel.saturation.floatValue += 0.01f
                    changeBitmap(viewModel, sharedVM)
                }
            },
            imageVector = ImageVector.vectorResource(id = R.drawable.svg_plus),
            contentDescription = "Plus"
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditScreenPreview() {
    DOKATheme {
        SaturationScreen()
    }
}

fun changeBitmap(viewModel: SaturationViewModel, sharedVM: MainViewModel) {
    val originalBitmap = sharedVM.changedBitmap
    sharedVM.currentBitmap = originalBitmap?.let { bitmap ->
        changeBitmapSaturationOld(bitmap, viewModel.saturation.floatValue)
    }
    sharedVM.saturation.floatValue = viewModel.saturation.floatValue
}