package com.searchtracks.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.searchtracks.R
import com.stfalcon.frescoimageviewer.ImageViewer

@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String {
    return Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
    )
}

inline fun <reified T : Any> Activity.launchActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> Context.launchActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)


fun Context.showImagesInFullScreen(list: ArrayList<String>, position: Int = 0) {
    ImageViewer.Builder(this, list)
            .setStartPosition(position)
            .setBackgroundColorRes(R.color.colorBlack)
            .show()
}

fun Activity.setActivityResultOk(options: Bundle? = null) {
    val intent = Intent()
    intent.putExtra("detail", options)
    this.setResult(RESULT_OK, intent)
}

fun Activity.finishActivityResultOk() {
    val intent = Intent()
    this.setResult(RESULT_OK, intent)
    finish()
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    val toast = Toast.makeText(this, text, duration)
    val view = toast.view
    view.setBackgroundColor(Color.GRAY)

    val txtView = (view as LinearLayout).findViewById<TextView>(android.R.id.message)
    txtView.setTextColor(Color.WHITE)

    toast.show()
}

fun Context.showToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    val toast = Toast.makeText(this, getString(resId), duration)
    val view = toast.view
    view.setBackgroundColor(Color.GRAY)

    val txtView = (view as LinearLayout).findViewById<TextView>(android.R.id.message)
    txtView.setTextColor(Color.WHITE)

    toast.show()
}

fun Context.setDrawable(resId: Int) {
    ContextCompat.getDrawable(this, resId)
}

fun Context.getScreenHeight(): Int {
    val resource = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    var statusBarHeight = 0
    if (resource > 0) {
        statusBarHeight = this.resources.getDimensionPixelSize(resource)
    }
    return this.resources.displayMetrics.heightPixels - statusBarHeight
}