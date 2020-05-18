package com.yxl.retrofitconverter.model
import com.google.gson.annotations.SerializedName

data class MusicRankingListModel(
    @SerializedName("bg_color")
    var mBgColor: String,
    @SerializedName("bg_pic")
    var mBgPic: String,
    @SerializedName("color")
    var mColor: String,
    @SerializedName("comment")
    var mComment: String,
    @SerializedName("content")
    var mContent: List<RankingListContent>,
    @SerializedName("count")
    var mCount: Int,
    @SerializedName("name")
    var mName: String,
    @SerializedName("pic_s192")
    var mPicS192: String,
    @SerializedName("pic_s210")
    var mPicS210: String,
    @SerializedName("pic_s260")
    var mPicS260: String,
    @SerializedName("pic_s444")
    var mPicS444: String,
    @SerializedName("type")
    var mType: Int,
    @SerializedName("web_url")
    var mWebUrl: String
)

data class RankingListContent(
    @SerializedName("album_id")
    var mAlbumId: String,
    @SerializedName("album_title")
    var mAlbumTitle: String,
    @SerializedName("all_rate")
    var mAllRate: String,
    @SerializedName("author")
    var mAuthor: String,
    @SerializedName("biaoshi")
    var mBiaoshi: String,
    @SerializedName("pic_big")
    var mPicBig: String,
    @SerializedName("pic_small")
    var mPicSmall: String,
    @SerializedName("rank_change")
    var mRankChange: String,
    @SerializedName("song_id")
    var mSongId: String,
    @SerializedName("title")
    var mTitle: String
)