package com.example.our_tik_tok.base.ui

import androidx.lifecycle.ViewModel
import com.example.our_tik_tok.base.extensions.RxjavaLifecycle
import io.reactivex.rxjava3.disposables.Disposable

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/24
 */
abstract class BaseViewModel : ViewModel(), RxjavaLifecycle {
    private val mDisposableList = arrayListOf<Disposable>()

    override fun onAddRxjava(disposable: Disposable) {
        mDisposableList.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        mDisposableList.filter { !it.isDisposed }
            .forEach { it.dispose() }
        mDisposableList.clear()
    }
}