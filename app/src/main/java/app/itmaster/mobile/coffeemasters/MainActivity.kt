package app.itmaster.mobile.coffeemasters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import app.itmaster.mobile.coffeemasters.data.DataManager
import app.itmaster.mobile.coffeemasters.ui.theme.CoffeeMastersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Garantiza que ante cambios de configuración (pasar a landscape)
        // no generan un nuevo DataManager y se reusa el que ya existía
        var dataManager = ViewModelProvider(this)
            .get(DataManager::class.java)

        setContent {
            CoffeeMastersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(dataManager)
                }
            }
        }
    }
}
