package com.dokaLocal.ui.screens.edit

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.dokaLocal.ImageSettings
import com.dokaLocal.MainViewModel
import com.dokaLocal.R
import com.dokaLocal.ui.theme.BottomPanelColor
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.ui.theme.RedColor
import com.dokaLocal.ui.theme.TouchPanelFrameColor
import com.dokaLocal.ui.theme.WhiteColor
import com.dokaLocal.util.rotate


@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel()
) {

    BackHandler {
        navigateBack()
    }

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
                    .padding(vertical = 60.dp)
                    .drawBehind {
                        // Draw the dotted border around the composable
                        drawRoundRect(
                            color = RedColor,
                            topLeft = Offset.Zero,
                            size = size,
                            cornerRadius = CornerRadius(10.dp.toPx()),
                            style = Stroke(
                                width = 3.dp.toPx(), // Border width
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(20f, 20f),
                                    0f
                                )
                            )
                        )
                    },
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
                    .padding(vertical = 10.dp),
                sharedVM = sharedVM, navigateNext = navigateNext,
                navigateBack = navigateBack
            )
        }
    }
}


@Composable
fun MainFrame(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: EditViewModel = hiltViewModel()
) {
    BoxWithConstraints(
        modifier = modifier
            .clipToBounds()
    ) {
        val density = LocalDensity.current
        LaunchedEffect(Unit) {
            viewModel.setFrameSize(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())

            val imageFrameWidth = constraints.maxWidth * 0.7f
            val imageFrameHeight = constraints.maxHeight * 0.7f
            val imageFrameWidthDp: Dp = with(density) { imageFrameWidth.toDp() }
            val imageFrameHeightDp: Dp = with(density) { imageFrameHeight.toDp() }

            if (sharedVM.savedImagesSettings.value.rotation == 0f || sharedVM.savedImagesSettings.value.rotation == 180f) {
                viewModel.setImageFrameSize(
                    imageFrameWidth,
                    imageFrameHeight,
                    imageFrameWidthDp,
                    imageFrameHeightDp
                )
            } else {
                viewModel.setImageFrameSize(
                    imageFrameHeight,
                    imageFrameWidth,
                    imageFrameHeightDp,
                    imageFrameWidthDp
                )
            }

            sharedVM.currentBitmap?.let {
                if (sharedVM.savedImagesSettings.value.rotation == 0f || sharedVM.savedImagesSettings.value.rotation == 180f)
                    viewModel.setRealImageSize(it.width, it.height, density)
                else
                    viewModel.setRealImageSize(it.height, it.width, density)
            }

            viewModel.realImageSize.value?.let {
                val startX = ((constraints.maxWidth - it.width) / 2)
                val startY = ((constraints.maxHeight - it.height) / 2)

                if (viewModel.offset.value == null) {
                    viewModel.updateOffset(Offset(startX, startY))
                }
            }
        }

        viewModel.realImageSize.value?.let { realImageSize ->
            realImageSize.widthDp?.let { widthDp ->
                realImageSize.heightDp?.let { heightDp ->
//                    sharedVM.finalImageWidth = widthDp * viewModel.zoom.value
//                    sharedVM.finalImageHeight = heightDp * viewModel.zoom.value

                    sharedVM.finalImageWidth = widthDp
                    sharedVM.finalImageHeight = heightDp

                    FrameWithImage(
                        modifier = Modifier
                            .size(width = widthDp, height = heightDp)
                            .offset {
                                viewModel.offset.value?.round() ?: Offset(0f, 0f).round()
                            }, sharedVM

                    )
                }
            }
        }
    }
}

@Composable
fun ImageWithDimensions(
    imageBitmap: Bitmap,
    sharedVM: MainViewModel,
    viewModel: EditViewModel = hiltViewModel()
) {

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()

    ) {
        if (!sharedVM.isDefault) {
            sharedVM.defaultImageWidth = constraints.maxWidth.toFloat()
            sharedVM.defaultImageHeight = constraints.maxHeight.toFloat()
            sharedVM.isDefault = true
        }

        Image(
            bitmap = imageBitmap.asImageBitmap(),
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
                    color = Color.Green,
                    shape = RoundedCornerShape(0.dp)
                ),
            contentDescription = "Image for edit",
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
fun FrameWithImage(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: EditViewModel = hiltViewModel()
) {

    Box(modifier = modifier) {
        if (sharedVM.originalBitmap != null)
            sharedVM.currentBitmap = sharedVM.originalBitmap

        sharedVM.currentBitmap?.let {
            val picture = it.rotate(viewModel.angle.value)
            sharedVM.finalImageRotate = viewModel.angle.value

            ImageWithDimensions(picture, sharedVM, viewModel)
        }
    }
}

fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix().apply {
        postRotate(degrees)
    }
    return Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
    )
}



@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel,
    viewModel: EditViewModel = hiltViewModel()
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
            text = "Secure with the rubber\nbands the dokacardboard\nwithing the doted lines",
            color = RedColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = BottomPanelColor,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(vertical = 16.dp)

        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back_up),
                contentDescription = "Button back",
                modifier = Modifier
                    .clickable { navigateBack() }
            )

            Spacer(modifier = Modifier.width(16.dp))

            TouchPanel(modifier = Modifier.weight(1f), sharedVM = sharedVM)

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.svg_check),
                    contentDescription = "Button Next",
                    modifier = Modifier
                        .clickable {

                            sharedVM.savedImagesSettings.value = ImageSettings(
                                viewModel.zoom.value,
                                viewModel.angle.value,
                                viewModel.offset.value!!.x,
                                viewModel.offset.value!!.y
                            )

                            sharedVM.originalBitmap = sharedVM.currentBitmap

//                            sharedVM.currentBitmap?.let {
//                                sharedVM.currentBitmap =
//                                    sharedVM.currentBitmap!!.applyRedMultiplyEffect()
//                            }

                            sharedVM.imageSize = viewModel.realImageSize.value
                            navigateNext()
                        }
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.svg_rotation),
                    contentDescription = "Rotate",
                    modifier = Modifier
                        .clickable {
                            viewModel.updateAngle()
                        }
                )
            }

        }
    }
}

@Composable
fun TouchPanel(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: EditViewModel = hiltViewModel()
) {
    val zoomState = sharedVM.zoomState

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MainBackgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = TouchPanelFrameColor,
                shape = RoundedCornerShape(10.dp) // Set the corner radius
            )
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { _, pan, gestureZoom, _ ->
                        sharedVM.zoomState = sharedVM.zoomState.copy(
                            offsetX = sharedVM.zoomState.offsetX + pan.x,
                            offsetY = sharedVM.zoomState.offsetY + pan.y,
                            zoom = (sharedVM.zoomState.zoom * gestureZoom).coerceIn(0.5f, 5f)
                        )

//                        val oldScale = viewModel.zoom.value
//                        val newScale =
//                            (viewModel.zoom.value * gestureZoom).coerceIn(0.5f..5f)
//                        if (oldScale == newScale) {
//                            val summed = viewModel.offset.value!! + pan
//                            viewModel.updateOffset(summed)
//                        } else {
////                            viewModel.updateAngle(viewModel.angle.value + gestureRotate)
//                            viewModel.updateZoom(newScale)
//                        }
                    }
                )
            }
    )
}

fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 4.dp,
    dashWidth: Dp = 8.dp,
    gapWidth: Dp = 13.dp,
    cap: StrokeCap = StrokeCap.Round
) = this.drawWithContent {
    val outline = shape.createOutline(size, layoutDirection, this)

    val path = Path()
    path.addOutline(outline)

    val stroke = Stroke(
        cap = cap,
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashWidth.toPx(), gapWidth.toPx()),
            phase = 0f
        )
    )

    this.drawContent()

    drawPath(
        path = path,
        style = stroke,
        color = color
    )
}

fun Bitmap.applyRedScale(): Bitmap? {
    val colorMatrix = ColorMatrix().apply {
        set(
            floatArrayOf(
                1f, 0f, 0f, 0f, 0f, // Red
                0f, 0f, 0f, 0f, 0f, // Green
                0f, 0f, 0f, 0f, 0f, // Blue
                0f, 0f, 0f, 1f, 0f  // Alpha
            )
        )
    }

    // Create a new bitmap with the same size as the original
    val redScaledBitmap = Bitmap.createBitmap(this.width, this.height, this.config)
    val canvas = Canvas(redScaledBitmap)
    val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(colorMatrix)
    }

    // Draw the original bitmap on the new canvas with the red scale color filter
    canvas.drawBitmap(this, 0f, 0f, paint)

    return redScaledBitmap
}

fun Bitmap.applyRedMultiplyEffect(): Bitmap? {
    // Apply red scale
    val redScaledBitmap = this.applyRedScale() ?: return null

    // Create a bitmap for the resulting image
    val resultBitmap = Bitmap.createBitmap(this.width, this.height, this.config)
    val canvas = Canvas(resultBitmap)

    // Draw a red rectangle over the canvas
    val paint = Paint().apply {
        setARGB(255, 255, 0, 0) // Red color
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

    // Set paint for blending mode
    paint.xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.MULTIPLY)

    // Draw the red scaled image over the red rectangle
    canvas.drawBitmap(redScaledBitmap, 0f, 0f, paint)

    return resultBitmap
}


fun invertColors(originalBitmap: Bitmap): Bitmap {
    val width = originalBitmap.width
    val height = originalBitmap.height
    val invertedImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val paint = Paint()
    val canvas = Canvas(invertedImage)

    // Create a color matrix for color inversion
    val colorMatrix = android.graphics.ColorMatrix().apply {
        setSaturation(0f) // Convert to grayscale
        val matrix = floatArrayOf(
            -1f, 0f, 0f, 0f, 255f, // Red
            0f, -1f, 0f, 0f, 255f, // Green
            0f, 0f, -1f, 0f, 255f, // Blue
            0f, 0f, 0f, 1f, 0f    // Alpha
        )
        postConcat(android.graphics.ColorMatrix(matrix))
    }

    paint.colorFilter = android.graphics.ColorMatrixColorFilter(colorMatrix)

    // Draw the original bitmap onto the canvas using the paint with color filter
    canvas.drawBitmap(originalBitmap, 0f, 0f, paint)

    return invertedImage
}

@Preview(
    showBackground = true, showSystemUi = true,
    device = "spec:width=411dp,height=901dp,dpi=420"
)
@Composable
fun EditScreenPreview() {
    DOKATheme {
        EditScreen()
    }
}
