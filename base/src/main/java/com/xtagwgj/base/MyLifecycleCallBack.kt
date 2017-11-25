package com.xtagwgj.base

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kelin.translucentbar.library.TranslucentBarManager

/**
 *  自定义的Lifecycle
 *  Created by xtagwgj on 2017/11/25.
 */

abstract class MyLifecycleCallBack : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        AppManager.addActivity(activity)

        //状态栏的显示
        initStatusBarStyle(activity)

        //存在标题栏
        activity.findViewById<Toolbar>(R.id.toolbar)?.let {

            //控制是否显示和隐藏 toolbar
            if (activity is AppCompatActivity) {
                activity.setSupportActionBar(activity.findViewById(R.id.toolbar))
                activity.supportActionBar?.setDisplayShowTitleEnabled(false)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.setActionBar(activity.findViewById(R.id.toolbar))
                activity.actionBar.setDisplayShowTitleEnabled(false)
            }

            //标题栏的显示
            initToolbar(activity, it)
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        AppManager.removeActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle?) {
    }

    /**
     *  状态栏 模式
     */
    open fun initStatusBarStyle(activity: Activity) {
        // ios 样式的沉浸模式
        TranslucentBarManager(activity).transparent(activity)

//         Log.e("BaseApplication", "${StatusLightUtils.tryLightStatus(activity)}")
    }

    /**
     *  控制 toolbar 中内容的具体呈现
     */
    open fun initToolbar(activity: Activity, toolbar: Toolbar) {

        //初始化标题文字
        activity.findViewById<TextView>(R.id.toolbar_title)?.run {
            text = activity.title
        }

        activity.findViewById<ImageView>(R.id.toolbar_back)?.run {

            visibility = if (activity::class.java == getMainActivityClass()) {
                View.GONE
            } else {
                setOnClickListener {
                    //返回上一个界面
                    AppManager.finishActivity(activity)
                }

                View.VISIBLE
            }
        }
    }

    abstract fun getMainActivityClass(): Class<Any>
}
