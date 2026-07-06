package com.ypm14.simpatik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ypm14.simpatik.ui.navigation.Navigation
import com.ypm14.simpatik.ui.theme.Simpatik14Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Simpatik14Theme {
                Surface(modifier = Modifier.fillMaxSize()) { Navigation() }
            }
        }
    }
}
