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

    fun checkLoginInfo() {
        SPUtils.getInstance().apply {

            isRememberPassword.value = getBoolean("isRememberPassword", false)
            if (isRememberPassword.value == true) {
                phone.value = getString("phone")
                password.value = getString("password")
                tryLogin.value = true
            }

        }
    }
}