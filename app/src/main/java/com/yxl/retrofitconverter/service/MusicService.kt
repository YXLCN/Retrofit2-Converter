package com.yxl.retrofitconverter.service

import com.yxl.retrofitconverter.converter.CustomFlowable
import com.yxl.retrofitconverter.model.MusicRankingListModel
import retrofit2.http.GET

interface MusicService {

    @GET("/musicRankings")
    fun musicRankings(): CustomFlowable<List<MusicRankingListModel>>

}