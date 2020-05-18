package com.yxl.retrofitconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.yxl.retrofitconverter.retrofit.RetrofitFactory
import com.yxl.retrofitconverter.service.MusicService
import com.yxl.retrofitconverter.service.WallPaperService
import com.yxl.retrofitconverter.utils.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_content.movementMethod = ScrollingMovementMethod()

        btn_req_model.setOnClickListener {
            RetrofitFactory.getInstance(this)
                .createService(WallPaperService::class.java)
                .getWallPaperList(1)
                .flowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Logger.e("on call succeed!! result:\n${it.javaClass}\n$it")
                    tv_content.text = it.toString()
                }, {
                    Logger.e("on call failed!! result:\n$it")
                    it.printStackTrace()
                })
        }

        btn_req_json.setOnClickListener {
            RetrofitFactory.getInstance(this)
                .createService(WallPaperService::class.java)
                .getWallPaperListByJson(1)
                .flowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Logger.e("on call succeed!! result:\n${it.javaClass}\n$it")
                    tv_content.text = it.toString()
                }, {
                    Logger.e("on call failed!! result:\n$it")
                })
        }
    }
}
