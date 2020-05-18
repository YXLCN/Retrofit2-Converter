package com.yxl.retrofitconverter.model
import com.google.gson.annotations.SerializedName


data class WallPaperModel(
    @SerializedName("list")
    var mList: List<WallPaperListModel>,
    @SerializedName("page")
    var mPage: Int,
    @SerializedName("pages")
    var mPages: Int,
    @SerializedName("size")
    var mSize: Int,
    @SerializedName("total")
    var mTotal: Int
)

data class WallPaperListModel(
    @SerializedName("height")
    var mHeight: Int,
    @SerializedName("host")
    var mHost: String,
    @SerializedName("id")
    var mId: String,
    @SerializedName("size")
    var mSize: Int,
    @SerializedName("tags")
    var mTags: List<Tag>,
    @SerializedName("thumbUrl")
    var mThumbUrl: String,
    @SerializedName("url")
    var mUrl: String,
    @SerializedName("width")
    var mWidth: Int
)

data class Tag(
    @SerializedName("code")
    var mCode: String,
    @SerializedName("id")
    var mId: String,
    @SerializedName("name")
    var mName: String
)