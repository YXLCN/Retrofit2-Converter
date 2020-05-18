package com.yxl.retrofitconverter.service

import com.yxl.retrofitconverter.converter.CustomFlowable
import com.yxl.retrofitconverter.model.WallPaperModel
import io.reactivex.Flowable
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Query

interface WallPaperService {

    @GET("/api/picture")
    fun getWallPaperList(@Query("page") page: Int): CustomFlowable<WallPaperModel>

    // 直接使用 flowable 也可以，为了避免需要正常使用 flowable 为返回值的情况，最好多自定义一个 CustomFlowable
    @GET("/api/picture")
    fun getWallPaperList2(@Query("page") page: Int): Flowable<WallPaperModel>

    // 不对返回结果拦截的情况
    @GET("/api/picture")
    fun getWallPaperListByJson(@Query("page") page: Int): CustomFlowable<JSONObject>

}