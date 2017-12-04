package com.xtagwgj.base.mvvm

import android.arch.lifecycle.*
import com.blankj.utilcode.util.LogUtils
import com.xtagwgj.base.entity.Status

/**
 * 存储和处理跟 UI 界面相关的数据
 * Created by xtagwgj on 2017/12/3.
 */
open class BaseViewModel : ViewModel(), IViewModel, LifecycleObserver {

    /**
     * 页面当前的状态
     */
    val status: MutableLiveData<Status> = MutableLiveData()

    /**
     * 要显示的吐司信息
     */
    val toast: MutableLiveData<String> = MutableLiveData()

    /**
     * 进入该页面要进行的操作，比如说开始定位。。。
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onStart() {
        LogUtils.a("ON_CREATE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        LogUtils.a("ON_STOP")
    }
}