package com.xtagwgj.spinetest.user

import android.arch.lifecycle.MutableLiveData
import com.blankj.utilcode.util.SPUtils
import com.xtagwgj.base.mvvm.BaseViewModel

/**
 * 登录页面的 ViewModel
 * Created by xtagwgj on 2017/12/4.
 */
class LoginViewModel : BaseViewModel() {

    val pageInt = MutableLiveData<Int>().apply { value = 0 }

    /**
     * 账号
     */
    val phone = MutableLiveData<String>().apply { value = "18566077938" }

    /**
     * 密码
     */
    val password = MutableLiveData<String>()

    /**
     * 是否记住密码
     */
    val isRememberPassword = MutableLiveData<Boolean>().apply { value = false }

    /**
     * 是否尝试自动登录
     */
    val tryLogin = MutableLiveData<Boolean>().apply { false }


    /**
     * 检查登录，填充账号密码信息
     * 1.判断当前是否是最新版本，不是最新版本的要清除登录的信息重新登录【保证数据的正确性】
     * 2.判断是否记住了密码，记住了密码就填充记录的账号密码信息
     * 3.判断是否需要自动登录
     */
    private fun checkLoginInfo() {
        SPUtils.getInstance().apply {
            isRememberPassword.value = getBoolean("isRememberPassword", false)
            if (isRememberPassword.value == true) {
                phone.value = getString("phone")
                password.value = getString("password")
                tryLogin.value = true
            }
        }
    }

    override fun onStart() {
        checkLoginInfo()
    }
}