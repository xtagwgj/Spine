package com.xtagwgj.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import com.blankj.utilcode.util.SPUtils
import java.util.*

/**
 * 用于设置保存语言及获取当前语言，重启APP等操作
 * Created by xtagwgj on 2017/12/9.
 */
/*
//Application中调用，用于初始化语言设置
    @Override
    public void onCreate() {
        super.onCreate();
        LocaleUtil.changeAppLanguage(this);
    }

//在Application中重写如下方法：用于当系统设置语言变化时进行语言设置
     @Override
     public void onConfigurationChanged(Configuration newConfig) {
         super.onConfigurationChanged(newConfig);
         Log.e("TAG", "onConfigurationChanged");
         LocaleUtil.setLanguage(mContext, newConfig);
     }

//在多语言设置界面设置APP语言
    LocaleUtil.changeAppLanguage(mContext, currentLanguage);

 */

object LocaleUtil {
    /**
     * 获取用户设置的Locale
     * @return Locale
     */
    val userLocale: Locale
        get() {
            val currentLanguage = SPUtils.getInstance().getInt("currentLanguage", 0)
            var myLocale = Locale.SIMPLIFIED_CHINESE
            when (currentLanguage) {
                0 -> myLocale = Locale.SIMPLIFIED_CHINESE
                1 -> myLocale = Locale.ENGLISH
                2 -> myLocale = Locale.TRADITIONAL_CHINESE
                else -> {
                }
            }
            return myLocale
        }

    /**
     * 设置语言：如果之前有设置就遵循设置如果没设置过就跟随系统语言
     */
    fun changeAppLanguage(context: Context?) {
        if (context == null) {
            return
        }
        val appContext = context.applicationContext

        val currentLanguage = SPUtils.getInstance().getInt("currentLanguage", -1)
        val myLocale: Locale
        // 0 简体中文 1 繁体中文 2 English
        myLocale = when (currentLanguage) {
            0 -> Locale.SIMPLIFIED_CHINESE
            1 -> Locale.TRADITIONAL_CHINESE
            2 -> Locale.ENGLISH
            else -> appContext.resources.configuration.locale
        }        // 本地语言设置
        if (needUpdateLocale(appContext, myLocale)) {
            updateLocale(appContext, myLocale)
        }
    }

    /**
     * 保存设置的语言
     *
     * @param currentLanguage index
     */
    fun changeAppLanguage(context: Context?, currentLanguage: Int) {
        if (context == null) {
            return
        }
        val appContext = context.applicationContext
        SPUtils.getInstance().put("currentLanguage", currentLanguage)
        var myLocale = Locale.SIMPLIFIED_CHINESE
        // 0 简体中文 1 繁体中文 2 English
        when (currentLanguage) {
            0 -> myLocale = Locale.SIMPLIFIED_CHINESE
            1 -> myLocale = Locale.TRADITIONAL_CHINESE
            2 -> myLocale = Locale.ENGLISH
        }
        // 本地语言设置
        if (LocaleUtil.needUpdateLocale(appContext, myLocale)) {
            LocaleUtil.updateLocale(appContext, myLocale)
        }
//        Toast.makeText(appContext, appContext.getString(R.string.set_success), Toast.LENGTH_SHORT).show()
        //fixme:回到app的主页面
//        restartApp(appContext,AppManager.currActivity().javaClass)
    }

    /**
     * 重启app生效
     *
     * @param context
     */
    fun restartApp(context: Context, mainClass: Class<Activity>) {
        val intent = Intent(context, mainClass)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    /**
     * 获取当前的Locale
     *
     * @param context Context
     * @return Locale
     */
    fun getCurrentLocale(context: Context): Locale {
        val locale: Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0有多语言设置获取顶部的语言
            locale = context.resources.configuration.locales.get(0)
        } else {
            locale = context.resources.configuration.locale
        }
        return locale
    }

    /**
     * 更新Locale
     *
     * @param context Context
     * @param locale  New User Locale
     */
    fun updateLocale(context: Context, locale: Locale) {
        if (needUpdateLocale(context, locale)) {
            val configuration = context.resources.configuration
            if (Build.VERSION.SDK_INT >= 19) {
                configuration.setLocale(locale)
            } else {
                configuration.locale = locale
            }
            val displayMetrics = context.resources.displayMetrics
            context.resources.updateConfiguration(configuration, displayMetrics)
        }
    }

    /**
     * 判断需不需要更新
     *
     * @param context Context
     * @param locale  New User Locale
     * @return true / false
     */
    fun needUpdateLocale(context: Context, locale: Locale?): Boolean {
        return locale != null && getCurrentLocale(context) != locale
    }

    /**
     * 当系统语言发生改变的时候还是继续遵循用户设置的语言
     *
     * @param context
     * @param newConfig
     */
    fun setLanguage(context: Context?, newConfig: Configuration) {
        if (context == null) return
        val appContext = context.applicationContext
        val currentLanguage = SPUtils.getInstance().getInt("currentLanguage", -1)
        val locale: Locale?
        // 0 简体中文 1 繁体中文 2 English
        when (currentLanguage) {
            0 -> locale = Locale.SIMPLIFIED_CHINESE
            1 -> locale = Locale.TRADITIONAL_CHINESE
            2 -> locale = Locale.ENGLISH
            else -> locale = appContext.resources.configuration.locale
        }
        // 系统语言改变了应用保持之前设置的语言
        if (locale != null) {
            Locale.setDefault(locale)
            val configuration = Configuration(newConfig)
            if (Build.VERSION.SDK_INT >= 19) {
                configuration.setLocale(locale)
            } else {
                configuration.locale = locale
            }
            appContext.resources.updateConfiguration(configuration, appContext.resources.displayMetrics)
        }
    }

}
