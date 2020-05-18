package com.yxl.retrofitconverter.converter

import com.yxl.retrofitconverter.model.BaseModel
import com.yxl.retrofitconverter.utils.Logger
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.HttpException
import retrofit2.Response

class CustomBodyObservable<T> : Observable<T> {
    private val responseObservable: Observable<Response<BaseModel<T>>>

    constructor(upstream: Observable<Response<BaseModel<T>>>) : super() {
        responseObservable = upstream
    }

    override fun subscribeActual(observer: Observer<in T>) {
        // 用 BodyObserver 装饰了原本的 observer，用于对异常情况进行统一处理
        responseObservable.subscribe(BodyObserver(observer))
    }

    private class BodyObserver<R> internal constructor(private val observer: Observer<in R>) :
        Observer<Response<BaseModel<R>>> {
        private var terminated = false

        override fun onSubscribe(disposable: Disposable) {
            observer.onSubscribe(disposable)
        }

        override fun onNext(response: Response<BaseModel<R>>) {
            Logger.i("BodyObserver onNext : ")
            val body = response.body()
            if (response.isSuccessful && body != null) {
                // 对异常情况进行统一处理
                if (body.mCode == 0) {
                    // 成功
                    Logger.i("onNext 请求成功, body.mResult : \n${body.mData}")
                    observer.onNext(body.mData!!)
                } else {
                    val apiErrorCode: Int = body.mCode
                    val apiErrorMessage: String? = body.mMessage
                    //业务失败
                    val t = DadaThrowable(apiErrorCode, apiErrorMessage)

                    try {
                        observer.onError(t)
                    } catch (inner: Throwable) {
                        Exceptions.throwIfFatal(inner)
                        RxJavaPlugins.onError(CompositeException(t, inner))
                    }
                }
            } else {
                terminated = true
                val t: Throwable = HttpException(response)
                try {
                    observer.onError(t)
                } catch (inner: Throwable) {
                    Exceptions.throwIfFatal(inner)
                    RxJavaPlugins.onError(CompositeException(t, inner))
                }
            }
        }

        override fun onComplete() {
            if (!terminated) {
                observer.onComplete()
            }
        }

        override fun onError(throwable: Throwable) {
            if (!terminated) {
                observer.onError(throwable)
            } else {
                // This should never happen! onNext handles and forwards errors automatically.
                val broken: Throwable = AssertionError(
                    "This should never happen! Report as a bug with the full stacktrace."
                )
                broken.initCause(throwable)
                RxJavaPlugins.onError(broken)
            }
        }
    }


    class DadaThrowable(val apiErrorCode: Int = -1, val apiErrorMessage: String? = "unknown error") :
        Throwable() {

        override val message: String
            get() = "$apiErrorCode, 错误原因 : $apiErrorMessage"

    }

}