package com.yxl.retrofitconverter.converter

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.yxl.retrofitconverter.model.BaseModel
import com.yxl.retrofitconverter.utils.Logger
import com.yxl.retrofitconverter.utils.TypeUtil
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import java.lang.reflect.Type

class CustomBodyConverter<T>(private val type: Type) :  Converter<ResponseBody, BaseModel<T>> {

    override fun convert(value: ResponseBody): BaseModel<T> {
        try {
            val respString = value.string()
//            Logger.i("convert ---> respString : $respString")

            val model = BaseModel<T>()
            val jsonObject = JSONObject(respString)
            model.mMessage = jsonObject.getString("message")
            model.mCode = jsonObject.getInt("code")

            val result = jsonObject.getString("data")
            if (!TextUtils.isEmpty(result) && !"null".equals(result, true)) {
                when (type) {
                    JsonObject::class.java,
                    JSONObject::class.java -> {
                        Logger.i("convert ---> JSONObject ")
                        model.mData = JSONObject(result) as T
                    }
                    JsonArray::class.java -> {
                        Logger.i("convert ---> JsonArray ")
                    }
                    else -> {
                        model.mData = Gson().fromJson(result, this.type)
                    }
                }
            } else {
                val rawType: Class<*> = TypeUtil.getRawType(type)
                if (rawType == String.javaClass) {
                    model.mData  = "" as T
                } else {
                    model.mData  = Gson().fromJson<T>("{}", type)
                }
            }
            Logger.i("convert ---> model : $model")

            return model
        } catch (e: Exception) {
            e.printStackTrace()
            return BaseModel.unknownError(e)!!
        }
    }

}