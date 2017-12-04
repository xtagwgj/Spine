package com.xtagwgj.spinetest.user

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xtagwgj.base.ActivityUtils
import com.xtagwgj.base.utils.ToastUtil
import com.xtagwgj.spinetest.R

/**
 * 登录页面 使用fragment 包括用户的登录，用户注册，修改密码
 * Created by xtagwgj on 2017/12/4.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var mViewModel: LoginViewModel

    companion object {
        val PAGE_LOGIN = 0
        val PAGE_SIGNUP = 1
        val PAGE_FORGET = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        initView()

//        MainActivity.doAction(this)
    }

    private fun initView() {
        //页面切换
        mViewModel.pageInt.observe(this, Observer { pageFlag ->
            val fragment = when (pageFlag) {

                    PAGE_SIGNUP -> {
                        SignUpFragment.newInstance()
                    }

                    PAGE_FORGET -> {
                        ForgetFragment.newInstance()
                    }

                    else -> LoginFragment.newInstance()
                }

            ActivityUtils.replaceFragmentInActivity(supportFragmentManager, fragment, R.id.contentFrame)
        })

        //显示提示信息
        mViewModel.toast.observe(this, Observer { toastMsg ->
            toastMsg?.let {
                ToastUtil.showToast(this, toastMsg)
            }
        })
    }

}