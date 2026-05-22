package dev.canem.nexusmoderandi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.canem.nexusmoderandi.ui.navigation.NexusNavGraph
import dev.canem.nexusmoderandi.ui.theme.NexusModerandiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexusModerandiTheme {
                NexusNavGraph()
            }
        }
    }
}
