package com.xtagwgj.spinetest

import com.xtagwgj.base.BaseApplication
import com.xtagwgj.base.MyLifecycleCallBack

/**
 * @author xtagwgj
 * @date 2017/11/25
 */

class MyApp : BaseApplication() {

    override fun getLifecycle(): MyLifecycleCallBack = LifeCallback()
}
