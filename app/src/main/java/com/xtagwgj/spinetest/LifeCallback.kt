package com.xtagwgj.spinetest

import com.xtagwgj.base.MyLifecycleCallBack

/**
 * Created by xtagwgj on 2017/11/25.
 */

class LifeCallback : MyLifecycleCallBack() {
    override fun getMainActivityClass(): Class<Any> = MainActivity::class.java as Class<Any>

//    override fun initStatusBarStyle(activity: Activity) {
//        Log.e("BaseApplication", "${StatusLightUtils.tryLightStatus(activity)}")
//    }
}
