package com.xtagwgj.base.mvvm

import android.app.Application
import android.arch.lifecycle.*
import com.xtagwgj.base.entity.Status

/**
 * 存储和处理跟 UI 界面相关的数据
 * Created by xtagwgj on 2017/12/3.
 */
open class BaseAppViewModel(application: Application) : AndroidViewModel(application), IViewModel, LifecycleObserver {

    /**
     * 页面当前的状态
     */
    val status: MutableLiveData<Status> = MutableLiveData()

    /**
     * 要显示的吐司信息
     */
    val toast: MutableLiveData<String> = MutableLiveData()


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onStart() {

    }
}