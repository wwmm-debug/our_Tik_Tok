package com.example.our_tik_tok.base.extensions


import com.example.our_tik_tok.base.network.ApiException
import com.example.our_tik_tok.base.network.ApiStatus
import com.example.our_tik_tok.base.network.ApiWrapper
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable

/**
 * 示例代码：
 * ```
 * ApiService.INSTANCE.getXXX()
 *     .subscribeOn(Schedulers.io())  // 线程切换
 *     .observeOn(AndroidSchedulers.mainThread())
 *     .mapOrCatchApiException {      // 当 errorCode 的值不为成功时抛错，并处理错误
 *         // 处理 ApiException 错误
 *     }
 *     .unSafeSubscribeBy {             // 如果是网络连接错误，则这里会默认处理
 *         // 成功的时候
 *     }
 *     .lifeCycle() // ViewModel 中带有的自动回收，或者使用 ViewModel 里面的 safeSubscribeBy 方法
 * ```
 *
 * [mapOrCatchApiException] 只会处理 [ApiException] ，如果你要处理其他网络错误，
 * 把 [mapOrCatchApiException] 替换为 [mapOrThrowApiException]：
 * ```
 *     .mapOrThrowApiException()
 *     .doOnError {
 *         if (it is ApiException) {
 *             // 处理 ApiException 错误
 *         } else {
 *             // 处理其他网络错误
 *         }
 *     }
 * ```
 *
 *
 */

fun <T : ApiStatus> Single<T>.throwApiExceptionIfFail(): Single<T> {
    return doOnSuccess { it.throwApiExceptionIfFail() }
}

fun <T : ApiStatus> Maybe<T>.throwApiExceptionIfFail(): Maybe<T> {
    return doOnSuccess {  it.throwApiExceptionIfFail()}
}

fun <T : ApiStatus> Observable<T>.throwApiExceptionIfFail(): Observable<T> {
    return doOnNext { it.throwApiExceptionIfFail() }
}

fun <T : ApiStatus> Flowable<T>.throwApiExceptionIfFail(): Flowable<T> {
    return doOnNext { it.throwApiExceptionIfFail() }
}

fun <E : Any, T : ApiWrapper<E>> Single<T>.mapOrThrowApiException(): Single<E> {
    return throwApiExceptionIfFail()
        .map { it.data}
}

fun <E : Any, T : ApiWrapper<E>> Maybe<T>.mapOrThrowApiException(): Maybe<E> {
    return throwApiExceptionIfFail()
        .map { it.data }
}

fun <E : Any, T : ApiWrapper<E>> Observable<T>.mapOrThrowApiException(): Observable<E> {
    return throwApiExceptionIfFail()
        .map { it.data }
}

fun <E : Any, T : ApiWrapper<E>> Flowable<T>.mapOrThrowApiException(): Flowable<E> {
    return throwApiExceptionIfFail()
        .map { it.data }
}

fun <T : ApiStatus> Single<T>.catchApiException(
    func: (ApiException) -> Unit
): Single<T> {
    return throwApiExceptionIfFail()
        .doOnError {
            if (it is ApiException) func.invoke(it)
        }
}

fun <T : ApiStatus> Maybe<T>.catchApiException(
    func: (ApiException) -> Unit
): Maybe<T> {
    return throwApiExceptionIfFail()
        .doOnError {
            if (it is ApiException) func.invoke(it)
        }
}

fun <T : ApiStatus> Observable<T>.catchApiException(
    func: (ApiException) -> Unit
): Observable<T> {
    return throwApiExceptionIfFail()
        .doOnError {
            if (it is ApiException) func.invoke(it)
        }
}

fun <T : ApiStatus> Flowable<T>.catchApiException(
    func: (ApiException) -> Unit
): Flowable<T> {
    return throwApiExceptionIfFail()
        .doOnError {
            if (it is ApiException) func.invoke(it)
        }
}

fun <E : Any, T : ApiWrapper<E>> Single<T>.mapOrCatchApiException(
    func: (ApiException) -> Unit
): Single<E> {
    return catchApiException(func)
        .map { it.data }
}

fun <E : Any, T : ApiWrapper<E>> Maybe<T>.mapOrCatchApiException(
    func: (ApiException) -> Unit
): Maybe<E> {
    return catchApiException(func)
        .map { it.data }
}

fun <E : Any, T : ApiWrapper<E>> Observable<T>.mapOrCatchApiException(
    func: (ApiException) -> Unit
): Observable<E> {
    return catchApiException(func)
        .map { it.data }
}

fun <E : Any, T : ApiWrapper<E>> Flowable<T>.mapOrCatchApiException(
    func: (ApiException) -> Unit
): Flowable<E> {
    return catchApiException(func)
        .map { it.data }
}

/**
 * 其实这个命名是直接抄的掌邮的，这个 safe 的意思是如果直接使用一个形参的 subscribe(onSuccess)，
 * 在收到上游错误时 Rxjava 会把错误直接抛给整个应用来处理，如果你没有配置 Rxjava 的全局报错，应用会直接闪退，
 * safe 就是指这个安全问题，目前因为生命周期问题，所以改名
 */

fun <T : Any> Single<T>.unSafeSubscribeBy(
    onError: (Throwable) -> Unit = {},
    onSuccess: (T) -> Unit = {}
): Disposable = subscribe(onSuccess, onError)

fun <T : Any> Maybe<T>.unSafeSubscribeBy(
    onError: (Throwable) -> Unit = {},
    onComplete: () -> Unit = {},
    onSuccess: (T) -> Unit = {}
): Disposable = subscribe(onSuccess, onError, onComplete)

fun <T : Any> Observable<T>.unSafeSubscribeBy(
    onError: (Throwable) -> Unit = {},
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
): Disposable = subscribe(onNext, onError, onComplete)

fun <T : Any> Flowable<T>.unSafeSubscribeBy(
    onError: (Throwable) -> Unit = {},
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
): Disposable = subscribe(onNext, onError, onComplete)

fun Completable.unSafeSubscribeBy(
    onError: (Throwable) -> Unit = {},
    onComplete: () -> Unit = {},
): Disposable = subscribe(onComplete, onError)

/**
 * 实现该接口，即代表该类支持自动关闭 Rxjava
 */
interface RxjavaLifecycle {

    fun onAddRxjava(disposable: Disposable)

    fun <T : Any> Single<T>.safeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onSuccess: (T) -> Unit = {}
    ): Disposable = subscribe(onSuccess, onError).also { onAddRxjava(it) }

    @Deprecated(
        "该类支持已实现 Rxjava 的生命周期，请使用 safeSubscribeBy代替",
        ReplaceWith("safeSubscribeBy()"))
    fun <T : Any> Single<T>.unSafeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onSuccess: (T) -> Unit = {}
    ): Disposable = subscribe(onSuccess, onError)

    fun <T : Any> Maybe<T>.safeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
        onSuccess: (T) -> Unit = {}
    ): Disposable = subscribe(onSuccess, onError).also { onAddRxjava(it) }

    @Deprecated(
        "该类支持已实现 Rxjava 的生命周期，请使用 safeSubscribeBy 代替",
        ReplaceWith("safeSubscribeBy()"))
    fun <T : Any> Maybe<T>.unSafeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
        onSuccess: (T) -> Unit = {}
    ): Disposable = subscribe(onSuccess, onError, onComplete)

    fun <T : Any> Observable<T>.safeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    ): Disposable = subscribe(onNext, onError, onComplete).also { onAddRxjava(it) }

    @Deprecated(
        "该类支持已实现 Rxjava 的生命周期，请使用 safeSubscribeBy 代替",
        ReplaceWith("safeSubscribeBy()"))
    fun <T : Any> Observable<T>.unSafeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    ): Disposable = subscribe(onNext, onError, onComplete)

    fun <T : Any> Flowable<T>.safeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    ): Disposable = subscribe(onNext, onError, onComplete).also { onAddRxjava(it) }

    @Deprecated(
        "该类支持已实现 Rxjava 的生命周期，请使用 safeSubscribeBy 代替",
        ReplaceWith("safeSubscribeBy()"))
    fun <T : Any> Flowable<T>.unSafeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    ): Disposable = subscribe(onNext, onError, onComplete)

    fun Completable.safeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
    ): Disposable = subscribe(onComplete, onError).also { onAddRxjava(it) }

    @Deprecated(
        "该类支持已实现 Rxjava 的生命周期，请使用 safeSubscribeBy 代替",
        ReplaceWith("safeSubscribeBy()"))
    fun Completable.unSafeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
    ): Disposable = subscribe(onComplete, onError)
}
