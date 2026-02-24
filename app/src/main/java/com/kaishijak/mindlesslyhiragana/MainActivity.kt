package com.kaishijak.mindlesslyhiragana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kaishijak.mindlesslyhiragana.navigation.DefaultNavGraph
import com.kaishijak.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
    }
}