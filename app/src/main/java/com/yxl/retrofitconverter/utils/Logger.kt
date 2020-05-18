package com.yxl.retrofitconverter.utils

import android.util.Log

class Logger {

    companion object {
        private const val TAG: String = "RetrofitConverter"

        fun i(msg: String) {
            Log.i(TAG, msg)
        }

        fun e(msg: String) {
            Log.e(TAG, msg)
        }
    }

}