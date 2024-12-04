package com.dokaLocal.ui.screens.settings

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.style.TextOverflow
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
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.DefaultColor
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.util.rotate


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    navigateExpTimer: () -> Unit = {},
    navigateExposure: () -> Unit = {},
    navigateSaturation: () -> Unit = {},
    navigateContrast: () -> Unit = {},
    navigateTint: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: SettingsViewModel = hiltViewModel(),
) {

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
                navigateExpTimer = navigateExpTimer,
                navigateBack = navigateBack,
                navigateExposure = navigateExposure,
                navigateSaturation = navigateSaturation,
                navigateContrast = navigateContrast,
                navigateTint = navigateTint
            )
        }
    }
}

@Composable
fun MainFrame(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel
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
fun FrameWithImage(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel
) {
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
    navigateExpTimer: () -> Unit,
    navigateExposure: () -> Unit,
    navigateSaturation: () -> Unit,
    navigateContrast: () -> Unit,
    navigateTint: () -> Unit,
    navigateBack: () -> Unit = {}
) {
    val buttonList = remember {
        listOf(
            ButtonModel("Exp. timer", R.drawable.svg_timer),
            ButtonModel("Exposure", R.drawable.svg_exposure),
            ButtonModel("Saturation", R.drawable.svg_saturation),
            ButtonModel("Contrast", R.drawable.svg_contrast),
            ButtonModel("Tint", R.drawable.svg_tint)
        )
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = BottomPanelColor,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(vertical = 16.dp, horizontal = 30.dp)

    ) {
        Row {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_check),
                contentDescription = "Button OK",
                modifier = Modifier
                    .clickable { navigateBack() }
                    .padding(bottom = 20.dp, start = 10.dp)
            )
        }

        LazyRow {
            itemsIndexed(buttonList) { index, _ ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(70.dp)
                        .clickable {
                            if (buttonList[index].title == "Exp. timer") navigateExpTimer()
                            if (buttonList[index].title == "Exposure") navigateExposure()
                            if (buttonList[index].title == "Saturation") navigateSaturation()
                            if (buttonList[index].title == "Contrast") navigateContrast()
                            if (buttonList[index].title == "Tint") navigateTint()
                        }
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = buttonList[index].image),
                        contentDescription = buttonList[index].title,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = buttonList[index].title,
                        fontSize = 14.sp,
                        maxLines = 1,
                        color = DefaultColor,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        }
    }

}

data class ButtonModel(val title: String, val image: Int)

@Preview(
    showBackground = true, showSystemUi = true,
    device = "spec:width=411dp,height=711dp,dpi=420"
)
@Composable
fun GreetingPreview() {
    DOKATheme {
        SettingsScreen()
    }
}
