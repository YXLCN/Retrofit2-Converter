package com.yxl.retrofitconverter.converter

import com.yxl.retrofitconverter.utils.Logger
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class RespConvertFactory: Converter.Factory() {

    // 获取请求body转换器，暂时无用
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    // 获取返回消息转换器
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val rawType = getRawType(type)
        Logger.i("responseBodyConverter --> rawType : $rawType")
        // 这里对相同的请求存在缓存，即一次获取 converter 失败后，不会再次从该 factory 获取 converter
        if (rawType != CustomFlowable::class.java) {
            Logger.i("responseBodyConverter --> INTERCEPT rawType: $rawType ")
            return null
        }

        return CustomBodyConverter<Any>(type)
    }
}