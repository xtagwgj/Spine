package com.xtagwgj.spinetest.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.xtagwgj.base.view.citylist.CityInfoBean

/**
 * MainActivity 的 model
 * Created by xtagwgj on 2017/11/25.
 */
class MainModel : ViewModel() {

    /**
     *  选择的城市信息
     */
    var selectCity: MutableLiveData<CityInfoBean> = MutableLiveData()


}