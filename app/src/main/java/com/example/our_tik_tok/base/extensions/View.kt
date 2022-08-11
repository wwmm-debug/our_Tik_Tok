package com.example.our_tik_tok.base.extensions


import android.view.View


fun View.gone() {
  visibility = View.GONE
}

fun View.invisible() {
  visibility = View.INVISIBLE
}

fun View.visible() {
  visibility = View.VISIBLE
}

/**
 * @param interval 毫秒为单位，点击间隔小于这个值监听事件无法生效
 * @param click 具体的点击事件
 */
fun View.setOnSingleClickListener(interval: Long = 500, click: (View) -> Unit) {
  setOnClickListener {
    val tag = getTag(423612342) as? Long
    if (System.currentTimeMillis() - (tag ?: 0L) > interval) {
      click(it)
    }
    it.setTag(423612342, System.currentTimeMillis())
  }
}