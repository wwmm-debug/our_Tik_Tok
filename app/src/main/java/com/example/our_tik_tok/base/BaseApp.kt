package com.example.our_tik_tok.base

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper


open class BaseApp : Application() {
  companion object {
    lateinit var appContext: Context
      private set
  }
  
  @CallSuper
  override fun onCreate() {
    super.onCreate()
    appContext = this
  }

}