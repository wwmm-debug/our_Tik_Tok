package com.example.our_tik_tok.base.extensions


import android.widget.Toast
import com.example.our_tik_tok.base.BaseApp



fun toast(s: CharSequence) {
  Toast.makeText(BaseApp.appContext, s, Toast.LENGTH_SHORT).show()
}

fun toastLong(s: CharSequence) {
  Toast.makeText(BaseApp.appContext, s, Toast.LENGTH_LONG).show()
}

fun String.toast() = toast(this)
fun String.toastLong() = toastLong(this)