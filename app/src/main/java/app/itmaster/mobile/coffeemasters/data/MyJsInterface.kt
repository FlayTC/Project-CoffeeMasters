package app.itmaster.mobile.coffeemasters.data


import android.webkit.JavascriptInterface


class MyJsInterface(private val onButtonPressed: () -> Unit) {

    @JavascriptInterface
    fun buttonPressed() {
        onButtonPressed.invoke()
    }
}