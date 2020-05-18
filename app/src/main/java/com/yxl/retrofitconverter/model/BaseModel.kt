package com.yxl.retrofitconverter.model
import com.google.gson.annotations.SerializedName

data class BaseModel<T>(
    @SerializedName("code")
    var mCode: Int = -1,
    @SerializedName("data")
    var mData: T? = null,
    @SerializedName("message")
    var mMessage: String? = "",
    @SerializedName("stamp")
    var mStamp: String? = "",
    @SerializedName("time")
    var mTime: String? = ""
) {

    companion object {
        fun <T> unknownError(error: Throwable): BaseModel<T>? {
            val baseModel: BaseModel<T> = BaseModel()
            baseModel.mCode = -1
            baseModel.mMessage = error.message
            return baseModel
        }
    }

    override fun toString(): String {
        return "BaseModel(mCode=$mCode, mData=$mData, mMessage='$mMessage', mStamp='$mStamp', mTime='$mTime')"
    }

}
