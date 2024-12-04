package com.dokaLocal.ui.screens.workshop_mode

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokaLocal.R
import com.dokaLocal.SharedPreferencesHelper
import com.dokaLocal.data.data_source.network.RemoteDataSource
import com.dokaLocal.ui.theme.DOKATheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkShopModeScreen(
    modifier: Modifier = Modifier,
    remoteDataSource: RemoteDataSource, // Inject RemoteDataSource
    onSaveAndExit: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    var idValue by remember { mutableStateOf(TextFieldValue("")) }
    var secretValue by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }  // State for loading
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sharedPreferencesHelper = SharedPreferencesHelper(context) // Create an instance of the helper
    val Red = colorResource(id = R.color.red) // or any other name you used for it

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(250.dp))

            OutlinedTextField(
                value = idValue,
                onValueChange = {
                    if (it.text.length <= 3) idValue = it
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Red,
                    unfocusedBorderColor = Red,
                    cursorColor = Color.White
                ),
                textStyle = TextStyle(color = Color.White, fontSize = 25.sp),
                modifier = Modifier
                    .width(100.dp)
                    .background(Red, shape = RoundedCornerShape(1.dp))
                    .padding(horizontal = 8.dp)
            )

            Text("DokaCarboard # [id]", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.height(80.dp))

            OutlinedTextField(
                value = secretValue,
                onValueChange = { secretValue = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Red,
                    unfocusedBorderColor = Red,
                    cursorColor = Color.White
                ),
                textStyle = TextStyle(color = Color.White, fontSize = 25.sp),
                modifier = Modifier
                    .width(300.dp)
                    .background(Red, shape = RoundedCornerShape(1.dp))
                    .padding(horizontal = 8.dp)
            )

            Text("Secret [token]", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.weight(1f))

            // Conditional Button or Loading Indicator
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.Red,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(vertical = 16.dp)
                )
            } else {
                Button(
                    onClick = {
                        if (idValue.text.isEmpty() || secretValue.text.isEmpty()) {
                            Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                        } else {
                            isLoading = true  // Set loading state to true when starting the request
                            coroutineScope.launch {
                                val response = remoteDataSource.getPicture(idValue.text.trim(), secretValue.text.trim())
                                isLoading = false  // Set loading state to false once the request completes

                                if (response.isSuccessful) {
                                    // Save the id and token in SharedPreferences
                                    sharedPreferencesHelper.saveIdAndToken(idValue.text, secretValue.text)
                                    sharedPreferencesHelper.setWorkshopMode(true)
                                    navigateBack()
                                } else {
                                    Toast.makeText(context, "ID and token are incorrect", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(60.dp)
                ) {
                    Text(
                        text = "Save and Exit",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkShopModeScreenPreview() {
    DOKATheme {
//        WorkShopModeScreen()
    }
}
