package com.xtagwgj.spinetest

import com.xtagwgj.base.MyLifecycleCallBack
import com.xtagwgj.spinetest.main.MainActivity

/**
 * Created by xtagwgj on 2017/11/25.
 */

class LifeCallback : MyLifecycleCallBack() {
    override fun getMainActivityClass(): Class<Any> = MainActivity::class.java as Class<Any>

}
