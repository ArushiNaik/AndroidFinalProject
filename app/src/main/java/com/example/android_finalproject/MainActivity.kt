package com.example.android_finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_finalproject.ui.navigation.ChamplainAppNavHost
import com.example.android_finalproject.ui.theme.Android_FinalProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Android_FinalProjectTheme {
                ChamplainAppNavHost()
            }
        }
    }
}
