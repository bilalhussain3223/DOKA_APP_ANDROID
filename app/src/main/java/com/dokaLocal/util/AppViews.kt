package com.dokaLocal.util

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.dokaLocal.ui.theme.DefaultColor
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.ui.theme.RedColor

@Composable
fun ButtonDefault(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    buttonType: Int,
    sharedVM: MainViewModel = hiltViewModel(),
    listener: () -> Unit // Change this to a regular lambda
) {
    var selected by remember { mutableStateOf(false) }
    val color = remember { Animatable(MainBackgroundColor) }
    LaunchedEffect(selected) {
        color.animateTo(if (selected) DefaultColor else ButtonBackgroundColor)
    }
    Box(
        modifier = modifier
            .height(60.dp)
            .background(
                if (enabled) if (buttonType == 0) Color.Black else RedColor else Color.Gray,
                shape = RoundedCornerShape(40.dp)
            )
            .clickable(
                enabled = enabled // Enable or disable click based on the checkbox state
            ) {
                if (buttonType == 0) {
                    sharedVM.isDefault = false
                }
                selected = !selected
                listener() // Call the listener lambda without `invoke`
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 30.dp),
            text = text,
            fontSize = 18.sp,
            color = if (buttonType == 0) Color.White else Color.Black,
            fontWeight = FontWeight.SemiBold
        )
    }
}
