package com.dokaLocal.ui.screens.source_picture

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.dokaLocal.MainViewModel
import com.dokaLocal.ui.theme.BottomPanelColor
import com.dokaLocal.ui.theme.ButtonBackgroundColor
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.DarkTextColor
import com.dokaLocal.ui.theme.MainBackgroundColor


@Composable
fun ImageSourceScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel()
) {
    BackHandler {
        navigateBack()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackgroundColor)
    ) {
        val (bottomPanel) = createRefs()
        Box(
            modifier = Modifier
                .constrainAs(bottomPanel) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.percent(0.25f)
                    // Set height to 1/4 of the screen
                }
                .padding(bottom = 42.dp)
        ) {
            BottomPanel(
                Modifier.align(Alignment.BottomCenter),
                navigateNext = navigateNext,
                navigateBack = navigateBack,
                sharedVM = sharedVM
            )
        }

    }
}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    viewModel: ImageSourceViewModel = hiltViewModel(),
    sharedVM: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.state.message) {
        viewModel.state.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = BottomPanelColor,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(16.dp)

    ) {
        if (viewModel.state.isLoading) {
            CustomCircularProgressIndicator(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
            )
            Text(
                modifier = Modifier
                    .padding(top = 21.dp, bottom = 34.dp),
                fontSize = 24.sp,
                text = "Loading",
                color = ButtonBackgroundColor
            )
            Box(
                modifier = modifier
                    .height(46.dp)
                    .width(231.dp)
                    .background(ButtonBackgroundColor, shape = RoundedCornerShape(40.dp))
                    .clickable {
                        navigateBack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "Cancel",
                    fontSize = 24.sp,
                    color = DarkTextColor
                )
            }
        } else {
            sharedVM.currentBitmap = viewModel.state.picture
            navigateNext.invoke()
        }
    }
}

@Composable
fun CustomCircularProgressIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1000, easing = LinearEasing),
            RepeatMode.Restart
        ), label = ""
    )

    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF6D2C2C), Color(0xFF391616)),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Box(modifier = modifier.size(40.dp)) {
        Canvas(modifier = Modifier.size(40.dp)) {
            val strokeWidth = 4.dp.toPx()
            val radius = size.minDimension / 2 - strokeWidth / 2
            val center = Offset(size.width / 2, size.height / 2)

            drawArc(
                brush = gradient,
                startAngle = animatedAngle,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(strokeWidth)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ImageSourcePreview() {
    DOKATheme {
        ImageSourceScreen()
    }
}