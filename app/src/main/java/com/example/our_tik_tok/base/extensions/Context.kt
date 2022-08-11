package com.example.our_tik_tok.base.extensions


import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.our_tik_tok.base.BaseApp


val appContext
  get() = BaseApp.appContext

inline fun <reified T : Activity> Context.startActivity() {
  val intent = Intent(this, T::class.java)
  if (this !is Activity) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  }
  startActivity(intent)
}