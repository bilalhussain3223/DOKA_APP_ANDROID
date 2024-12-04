package com.dokaLocal.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dokaLocal.MainViewModel
import com.dokaLocal.ui.theme.ButtonBackgroundColor
import com.dokaLocal.ui.theme.ButtonBackgroundblackColor
import com.dokaLocal.ui.theme.DefaultColor
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.ui.theme.RedColor

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    buttonType: Int,
    sharedVM: MainViewModel = hiltViewModel(),
    listener: () -> Unit
){
    var selected by remember { mutableStateOf(false) }
    val color = remember { androidx.compose.animation.Animatable(MainBackgroundColor) }
    LaunchedEffect(selected) {
        color.animateTo(if (selected) DefaultColor else ButtonBackgroundblackColor)
    }
    Box(
        modifier = modifier
            .fillMaxWidth() // Make the button take the full width
            .height(60.dp)
            .background(
                Color.Black, // Black background color
                shape = RoundedCornerShape(25.dp) // Rounded corners with 25 dp radius
            )
            .border(BorderStroke(2.dp, RedColor), RoundedCornerShape(25.dp)) // Red border with radius 25 dp
            .clickable(enabled = enabled) {
                selected = !selected
                if (buttonType == 0) {
                    sharedVM.isDefault = false
                }
                listener.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 30.dp),
            text = text,
            fontSize = 18.sp,
            color = RedColor, // Red text color
            fontWeight = FontWeight.SemiBold
        )
    }
}
