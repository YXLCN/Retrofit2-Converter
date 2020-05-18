package com.yxl.retrofitconverter.converter

import com.yxl.retrofitconverter.model.BaseModel
import com.yxl.retrofitconverter.utils.Logger
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RespAdapterFactory<T> : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        Logger.i("rawType: $rawType, returnType: $returnType")
        // 对返回类型非 CustomFlowable 的请求不分发 adapter
        if (rawType != CustomFlowable::class.java/* && rawType != Flowable::class.java*/) {
            return null
        }

        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        Logger.i("observableType: $observableType")

        return CustomCallAdapter<BaseModel<T>>(observableType)
    }

}

class CustomCallAdapter<R>: CallAdapter<R, Any> {

    private var type: Type

    constructor(type: Type) {
        this.type = type
    }

    override fun adapt(call: Call<R>): Any {
        val observable = CallExecuteObservable(call) as Observable<Response<BaseModel<R>>>
        val bodyObservable = CustomBodyObservable(observable)
        // 由于接口定义的返回值为CustomFlowable，所以这里需要用 CustomFlowable 进行装饰
        return CustomFlowable(bodyObservable.toFlowable(BackpressureStrategy.LATEST))
//        return bodyObservable.toFlowable(BackpressureStrategy.LATEST)
    }

    override fun responseType(): Type {
        return type
    }

}

