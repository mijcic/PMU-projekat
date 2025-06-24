package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import rs.ac.bg.etf.projekat.navigation.NavigationGraph
import rs.ac.bg.etf.projekat.ui.theme.ProjekatTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProjekatTheme {
                val navController = rememberNavController()
                NavigationGraph(navController)
            }
        }
        hideSystemUI()
    }

    @SuppressLint("WrongConstant", "NewApi")
    private fun hideSystemUI() {
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(android.view.WindowInsets.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}