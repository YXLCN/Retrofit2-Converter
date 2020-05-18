package com.yxl.retrofitconverter.converter

import io.reactivex.Flowable

class CustomFlowable<T> {

    var flowable: Flowable<T>

    constructor(flowable: Flowable<T>) {
        this.flowable = flowable
    }
}