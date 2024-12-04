package com.dokaLocal.ui.screens.splash

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.dokaLocal.MainViewModel
import com.dokaLocal.R
import com.dokaLocal.SharedPreferencesHelper
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.DefaultColor
import com.dokaLocal.ui.theme.WhiteColor
import com.dokaLocal.util.ButtonDefault
import com.dokaLocal.util.adjustedImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException


@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateEdit: () -> Unit = {},
    navigateToWorkshop: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),

) {
    val activity = LocalContext.current as? Activity
    var tapCount by remember { mutableStateOf(0) }
    val sharedPreferencesHelper = SharedPreferencesHelper(LocalContext.current)
    val isWorkshopModeEnabled = sharedPreferencesHelper.isWorkshopModeEnabled()

    BackHandler {
        activity?.finish()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(DefaultColor)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        val (topImage, checkboxText, viewTutorial, bottomPanel) = createRefs()

        // Top Image
        Image(
            painter = painterResource(id = R.drawable.ic_splash_image),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(topImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 80.dp)
                .fillMaxWidth()
                .then(
                    if (isWorkshopModeEnabled) Modifier else Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        tapCount++
                        Log.d("SplashScreen", "Tap count: $tapCount")
                        if (tapCount == 5) {
                            navigateToWorkshop()
                            tapCount = 0
                        }
                    }
                )
        )

        // Checkbox and text
        val uriHandler = LocalUriHandler.current
        val checkedState = sharedVM.checkedState.collectAsState()

        Row(
            modifier = Modifier
                .constrainAs(checkboxText) {
                    top.linkTo(topImage.bottom, margin = 30.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { sharedVM.toggleChecked() },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = Color.White,
                    checkedColor = Color.White,
                    checkmarkColor = Color.Black
                )
            )
            TextWithClickableLink(uriHandler)
        }

        // View Tutorial text
        Text(
            "View Tutorial",
            modifier = Modifier
                .constrainAs(viewTutorial) {
                    bottom.linkTo(bottomPanel.top, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .clickable {
                    uriHandler.openUri("https://www.youtube.com/@dokalab/videos")
                },
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        )

        // Bottom panel with buttons
        Box(
            modifier = Modifier
                .constrainAs(bottomPanel) {
                    bottom.linkTo(parent.bottom, margin = 30.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            BottomPanel(
                sharedVM = sharedVM,
                navigateNext = navigateNext,
                navigateEdit = navigateEdit,
                checkedState = checkedState.value,
                isWorkshopModeEnabled = isWorkshopModeEnabled // Pass the workshop mode state
            )
        }
    }
}



@Composable
fun TextWithClickableLink(uriHandler: UriHandler) {
    val annotatedText = buildAnnotatedString {
        append("I have read the ")
        pushStringAnnotation(
            tag = "URL",
            annotation = "https://dokalab.com/"
        )
        withStyle(
            style = SpanStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("safety instructions")
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let {
                    uriHandler.openUri(it.item)
                }
        },
        style = TextStyle(color = WhiteColor, fontSize = 20.sp),
    )
}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel = hiltViewModel(),
    navigateNext: () -> Unit = {},
    navigateEdit: () -> Unit = {},
    checkedState: Boolean,
    isWorkshopModeEnabled: Boolean // Added parameter to control button visibility
) {
    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        try {
            val inputStream = uri?.let { context.contentResolver?.openInputStream(it) }
            if (inputStream != null) {
                sharedVM.currentBitmap = BitmapFactory.decodeStream(inputStream).adjustedImage()
                navigateEdit.invoke()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Display "Upload from Cloud" button only if isWorkshopModeEnabled is true
        if (isWorkshopModeEnabled) {
            // The button is only enabled when checkedState is true
            ButtonDefault(
                text = "Upload from Cloud",
                enabled = checkedState, // Enabled only if checkedState is true
                buttonType = 0
            ) {
                sharedVM.clearData()
                navigateNext()
            }
        } else {
            // Display "Upload from Gallery" button only if isWorkshopModeEnabled is false
            ButtonDefault(
                text = "Upload from Gallery",
                enabled = checkedState, // Enabled only if checkedState is true
                buttonType = 0
            ) {
                sharedVM.clearData()
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        // If isWorkshopModeEnabled is false, show the "Upload from Gallery" button when checkedState is true
        if (!isWorkshopModeEnabled && checkedState) {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    DOKATheme {
//        SplashScreen()
    }
}

