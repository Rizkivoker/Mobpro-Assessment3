package org.d3if3168.appassessment3

 import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
 import org.d3if3168.appassessment3.system.navigation.NavigationGraph
 import org.d3if3168.appassessment3.ui.screen.BaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationGraph()
        }
    }
}