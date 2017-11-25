package com.xtagwgj.base

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * 应用基类
 * 尽量少的初始化，留给子类来操作
 * Created by xtagwgj on 2017/11/18.
 */

abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //初始化工具类
        Utils.init(this)

        //注册生命周期的回调，处理各 activity 的共有事件
        registerActivityLifecycleCallbacks(getLifecycle())
    }

    abstract fun getLifecycle(): ActivityLifecycleCallbacks

}