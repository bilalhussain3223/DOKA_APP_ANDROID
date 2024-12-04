package com.dokaLocal

import android.app.NotificationManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.dokaLocal.ui.screens.done.DoneScreen
import com.dokaLocal.ui.screens.source_picture.ImageSourceScreen
import com.dokaLocal.ui.screens.workshop_mode.WorkShopModeScreen
import com.dokaLocal.ui.theme.DOKATheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            DOKATheme {
                NavigationComponent(navigator = Navigator())
//                DoneScreen()
            }
//            setContentView(R.layout.activity_main)

            hideSystemUI()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    private fun hideSystemUI() {
        //keep screen from sleep
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        //hide nav bar
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())


        //        full brightness
        val window = window
        val layoutParams= window.attributes
        layoutParams.screenBrightness =1.0f
        window.attributes = layoutParams
    }


    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        DOKATheme {
            ImageSourceScreen()
        }
    }
}