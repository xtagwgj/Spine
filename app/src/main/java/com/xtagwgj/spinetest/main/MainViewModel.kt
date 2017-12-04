package com.xtagwgj.spinetest.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.xtagwgj.base.SingleLiveEvent
import com.xtagwgj.base.mvvm.BaseViewModel
import com.xtagwgj.base.view.citylist.CityInfoBean

/**
 * MainActivity 的 model
 * Created by xtagwgj on 2017/11/25.
 */
class MainViewModel : BaseViewModel() {

    /**
     *  选择的城市信息
     */
    val selectCity: MutableLiveData<CityInfoBean> = MutableLiveData()

    /**
     * 金额信息
     */
    val money: MutableLiveData<Float> = MutableLiveData()

    /**
     * 选择城市的点击事件
     */
    val selectCityEvent: SingleLiveEvent<Void> = SingleLiveEvent()


    val cityCode: LiveData<String> = Transformations.switchMap(selectCity, {
        val result: MutableLiveData<String> = MutableLiveData()
        result.value = it.toString() + " changed"
        result
    })


}