package app.itmaster.mobile.coffeemasters.pages

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.itmaster.mobile.coffeemasters.data.MyJsInterface
import kotlinx.coroutines.delay


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun InfoPage() {
    var snackbarVisible by remember { mutableStateOf(false) }

    val jsInterface = MyJsInterface {
        println("Se apretó el botón")
        snackbarVisible = true
    }

    AndroidView(
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                addJavascriptInterface(jsInterface, "Android")
                webViewClient = WebViewClient()
                webChromeClient = object : WebChromeClient() {
                    override fun onJsAlert(
                        view: WebView,
                        url: String,
                        message: String,
                        result: JsResult
                    ): Boolean {
                        println("Alerta de JS: $message")
                        return true
                    }
                }

                loadUrl("file:///android_asset/index.html")
            }
        }
    )
    LaunchedEffect(snackbarVisible) {
        if (snackbarVisible) {
            delay(5000)
            snackbarVisible = false
        }
    }

    if (snackbarVisible) {
        Snackbar(
            modifier = Modifier
                .padding(8.dp)
                .offset(y = 264.dp),
        ) {
            Text("We receive your feedback, thank you!")
        }
    }
}
