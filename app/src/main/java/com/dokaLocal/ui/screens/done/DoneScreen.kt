package com.dokaLocal.ui.screens.done

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.dokaLocal.MainViewModel
import com.dokaLocal.R
import com.dokaLocal.ui.screens.edit.EditViewModel
import com.dokaLocal.ui.theme.BottomPanelColor
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.ui.theme.RedColor
import com.dokaLocal.util.ButtonDefault
import com.dokaLocal.util.CustomButton
import kotlinx.coroutines.launch


//@Composable
//fun DoneScreen(
//    modifier: Modifier = Modifier,
//    navigateBack: () -> Unit = {},
//    navigateNext: () -> Unit = {},
//    sharedVM: MainViewModel = hiltViewModel()
//) {
//    BackHandler {
//        navigateBack()
//    }
//
//    ConstraintLayout(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MainBackgroundColor)
//    ) {
//        val (doneView, bottomPanel) = createRefs()
////        Box(modifier = Modifier
////            .constrainAs(doneView) {
////                top.linkTo(parent.top)
////                bottom.linkTo(bottomPanel.top)
////                start.linkTo(parent.start)
////                end.linkTo(parent.end)
////
////                // Adding vertical bias to center mainFrame in the available space
////                top.linkTo(parent.top)
////                bottom.linkTo(bottomPanel.top)
////                verticalChainWeight = 0.5f
////            }) {
////            //DoneView()
////        }
//
//        Box(
//            modifier = Modifier
//                .constrainAs(bottomPanel) {
//                    bottom.linkTo(parent.bottom)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    height = Dimension.percent(0.25f) // Set height to 1/4 of the screen
//                }
//        ) {
//            BottomPanel(sharedVM = sharedVM) { navigateNext() }
//        }
//
//    }
//
//}
//
////@Composable
////fun DoneView(modifier: Modifier = Modifier) {
////    Column(modifier = modifier) {
////        Image(
////            modifier = Modifier.size(164.dp),
////            imageVector = ImageVector.vectorResource(R.drawable.svg_done),
////            contentDescription = "Done",
////        )
////
////    }
////}
//
//@Composable
//fun BottomPanel(
//    modifier: Modifier = Modifier,
//    sharedVM: MainViewModel,
//    navigateNext: () -> Unit = {}
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//            .background(
//                color = BottomPanelColor,
//                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
//            )
//            .padding(vertical = 16.dp)
//
//    ) {
//        Text(
//            modifier = Modifier
//                .wrapContentWidth()
//                .padding(horizontal = 10.dp),
//            text = "Wash photo for 2 minutes",
//            fontSize = 24.sp,
//            color = RedColor,
//            maxLines = 2,
//            fontWeight = FontWeight.SemiBold
//        )
//        Spacer(modifier = Modifier.weight(1f))
//        ButtonDefault(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 16.dp, horizontal = 30.dp),
//            text = "Select new photo",
//            true,
//            1
//        ) {
//            sharedVM.clearData()
//            navigateNext()
//        }
//        Spacer(modifier = Modifier.weight(1f))
//    }
//}
//
//
//@Preview(
//    showBackground = true, showSystemUi = true,
//    device = "spec:width=411dp,height=901dp,dpi=420"
//)
//@Composable
//fun GreetingPreview() {
//    DOKATheme {
//        DoneScreen()
//    }
//}
@Composable
fun DoneScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    navigateNext: () -> Unit = {},
    navigateEdit: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    editVM: EditViewModel = hiltViewModel(),
) {
    BackHandler {
        navigateBack()
    }

    // Creating a state to hold the delete result
    val deleteResult = remember { mutableStateOf<Result<Boolean>?>(null) }

    // The DoneScreen UI
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackgroundColor)
            .navigationBarsPadding()
    ) {
        val (doneView, bottomPanel) = createRefs()

        // Done text
        Box(
            modifier = Modifier
                .constrainAs(doneView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomPanel.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    verticalChainWeight = 1f
                }
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "¡Done!",
                fontSize = 40.sp,
                color = RedColor,
                fontWeight = FontWeight.Bold
            )
        }

        // Bottom panel with buttons
        Column(
            modifier = Modifier
                .constrainAs(bottomPanel) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .background(
                    color = BottomPanelColor,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(vertical = 16.dp)
                .navigationBarsPadding() // To ensure no overlap with system bars
        ) {
            BottomPanel(
                sharedVM = sharedVM,
                editVM = editVM,
                navigateNext = navigateNext,
                navigateEdit = navigateEdit,
                deleteResult = deleteResult.value // Pass the result from the state
            )
        }
    }
}

// BottomPanel composable that takes deleteResult as a parameter
////@Composable
////fun BottomPanel(
////    modifier: Modifier = Modifier,
////    sharedVM: MainViewModel,
////    editVM: EditViewModel,
////    navigateNext: () -> Unit = {},
////    navigateEdit: () -> Unit = {},
////    deleteResult: Result<Boolean>? // Observed result of deletePicture()
////) {
////    val scope = rememberCoroutineScope() // To launch the delete function in a coroutine scope
////
////    Column(
////        horizontalAlignment = Alignment.CenterHorizontally,
////        modifier = modifier
////            .fillMaxWidth()
////            .fillMaxHeight()
////            .background(
////                color = BottomPanelColor,
////                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
////            )
////            .padding(vertical = 16.dp)
////            .navigationBarsPadding()
////    ) {
////        Spacer(modifier = Modifier.weight(1f))
////
////        // First button - "Select new photo"
////        CustomButton(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(vertical = 16.dp, horizontal = 30.dp),
////            text = "Repeat Photo",
////            enabled = true,
////            buttonType = 1
////        ) {
////            // Ensure that this button doesn't trigger the delete operation
//////            sharedVM.clearData()
////            navigateEdit()
////        }
////
////        // Second button - "Complete"
////        ButtonDefault(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(vertical = 16.dp, horizontal = 30.dp),
////            text = "New Photo",
////            enabled = true,
////            buttonType = 2 // Indicating the "Complete" button type
////        ) {
////            // Trigger the deletion only when the "Complete" button is pressed
////            scope.launch {
////                // Now delete only when the user clicks the button
////                val result = editVM.deletePicture()
////                if (result.isSuccess) {
////                    navigateNext()
////                } else {
////                    // Handle failure (e.g., show a toast or some other UI indication)
////                    // Toast.makeText(LocalContext.current, "Error deleting picture", Toast.LENGTH_SHORT).show()
////                }
////            }
////        }
////
////        Spacer(modifier = Modifier.weight(2f))
////    }
//}


@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    editVM: EditViewModel,
    navigateNext: () -> Unit = {},
    navigateEdit: () -> Unit = {},
    navigateBack: () -> Unit = {},
    deleteResult: Result<Boolean>? // Observed result of deletePicture()
) {
    val scope = rememberCoroutineScope() // To launch the delete function in a coroutine scope

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp), // Reducing the space between items
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BottomPanelColor,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(vertical = 5.dp)
            .navigationBarsPadding()
    ) {
        // "Wash photo for Two minutes" text
        Text(
            text = "Wash photo for Two minutes",
            color = RedColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            modifier = Modifier.padding(bottom = 8.dp) // Space between text and buttons
        )

        // "Repeat Photo" button
        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            text = "Repeat Photo",
            enabled = true,
            buttonType = 1
        ) {
            navigateEdit()
        }

        // "New Photo" button
        ButtonDefault(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            text = "New Photo",
            enabled = true,
            buttonType = 2 // Indicating the "New Photo" button type
        ) {
            scope.launch {
                // Now delete only when the user clicks the button
                val result = editVM.deletePicture()
                if (result.isSuccess) {
                    navigateNext()
                } else {
                    // Handle failure (e.g., show a toast or some other UI indication)
                    // Toast.makeText(LocalContext.current, "Error deleting picture", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
