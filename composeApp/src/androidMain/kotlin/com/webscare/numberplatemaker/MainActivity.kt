package com.webscare.numberplatemaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.webscare.numberplatemaker.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            // Senior Approach: Yahan androidContext pass karein
            KoinApplication(application = {
                androidContext(this@MainActivity) // Yeh line error fix karegi
                modules(appModules)
            }) {
                App()
            }
        }
    }
}