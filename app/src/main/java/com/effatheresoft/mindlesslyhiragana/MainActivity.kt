package com.effatheresoft.mindlesslyhiragana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.effatheresoft.mindlesslyhiragana.ui.navigation.DefaultNavHost
import com.effatheresoft.mindlesslyhiragana.ui.navigation.Route
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MindlesslyHiraganaTheme {
                DefaultNavHost(navController, Route.Home)
            }
        }
    }
}

