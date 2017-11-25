package com.xtagwgj.base.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 浅色状态栏（白底黑字）
 * Created by xtagwgj on 2017/11/25.
 */

object StatusLightUtils {

    /**
     *  Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
     *  Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
     */

    private
    val isFlymeV4OrAbove: Boolean
        get() {
            val displayId = Build.DISPLAY
            if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
                val displayIdArray = displayId.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                displayIdArray
                        .filter { it.matches("^[4-9]//.(//d+//.)+//S*".toRegex()) }
                        .forEach { return true }
            }
            return false
        }

    /**
     * MIUI V6对应的versionCode是4
     * MIUI V7对应的versionCode是5
     */
    private val isMIUIV6OrAbove: Boolean
        get() {
            val miuiVersionCodeStr = getSystemProperty("ro.miui.ui.version.code")
            if (!TextUtils.isEmpty(miuiVersionCodeStr)) {
                try {
                    val miuiVersionCode = Integer.parseInt(miuiVersionCodeStr)
                    if (miuiVersionCode >= 4) {
                        return true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return false
        }

    /**
     * Android Api 23以上
     */
    private val isAndroidMOrAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    fun tryLightStatus(activity: Activity): Boolean {

        if (isMIUIV6OrAbove) {
            setMIUILightStatusBar(activity)
            return true
        }
        if (isFlymeV4OrAbove) {
            setFlymeLightStatusBar(activity)
            return true
        }

        if (isAndroidMOrAbove) {
            setAndroidNativeLightStatusBar(activity)
            return true
        }
        return false
    }

    //获取系统信息
    private fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop " + propName)
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return line
    }

    @SuppressLint("PrivateApi")
    private fun setMIUILightStatusBar(activity: Activity): Boolean {
        var result = false

        try {
            val clazz = activity.window.javaClass
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            //状态栏透明且黑色字体
            extraFlagField.invoke(activity.window, darkModeFlag, darkModeFlag)
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    private fun setFlymeLightStatusBar(activity: Activity?): Boolean {
        var result = false
        if (activity != null) {
            try {
                val lp = activity.window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = value or bit
                meizuFlags.setInt(lp, value)
                activity.window.attributes = lp
                result = true
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return result
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun setAndroidNativeLightStatusBar(activity: Activity) {
        val decor = activity.window.decorView
        decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}
