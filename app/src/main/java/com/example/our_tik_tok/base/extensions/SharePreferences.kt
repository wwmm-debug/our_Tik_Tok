package com.example.our_tik_tok.base.extensions


import android.content.Context
import android.content.SharedPreferences
import com.example.our_tik_tok.base.BaseApp


/**
 * 快捷得到方式
 */
fun Context.getSp(name: String): SharedPreferences {
    return getSharedPreferences(name, Context.MODE_PRIVATE)
}

/**
 * 全局通用的 Sp，用于整个应用内传递数据，重要数据以及大量数据请不要使用该 SP 保存
 *
 * 请在下面写上传递的 key 值，以 SP_模块名_作用名 开头命名，后面还可以细分
 *
 * 注意：这个是给整个应用全局使用的！
 */
val defaultSp: SharedPreferences
    get() = BaseApp.appContext.getSharedPreferences("defaultSp", Context.MODE_PRIVATE)

const val SP_TEST_DEMO = "这只是一个用于演示的例子"


fun SharedPreferences.edit(block: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.block()
    editor.apply()
}