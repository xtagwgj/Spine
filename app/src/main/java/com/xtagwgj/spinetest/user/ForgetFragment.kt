package com.xtagwgj.spinetest.user

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xtagwgj.base.extensions.isPhone
import com.xtagwgj.spinetest.R
import kotlinx.android.synthetic.main.fragment_forget.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 忘记密码页面
 */
class ForgetFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_forget, container, false)

        return rootView
    }


    private fun initEventListener() {
        bt_modifyPassword.setOnClickListener(this)
        tv_loginNow.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            bt_modifyPassword.id -> {
                checkAndModifyPassword()
            }

            tv_loginNow.id -> {
                mViewModel.pageInt.value = LoginActivity.PAGE_LOGIN
            }

            else -> {

            }
        }
    }

    private fun checkAndModifyPassword() {
        if (et_phone.isPhone().not()) {
            mViewModel.toast.value = "手机号输入错误"
            return
        }

        val phone = et_phone.text.toString().trim()
        val password = et_password.text.toString().trim()

        if (password.length < 4) {
            mViewModel.toast.value = "密码最少4位"
            return
        }

        //特定账号注册成功
        if (phone == "18566077938" && password == "111111") {
            mViewModel.toast.value = "密码修改成功"

            //停 1s 后，回到登录页进行登录
            doAsync {
                SystemClock.sleep(1000)
                uiThread {
                    mViewModel.apply {
                        //设置要登录的账号，密码
                        this.phone.value = phone
                        this.password.value = password
                        tryLogin.value = true

                        //回到前一页
                        pageInt.value = LoginActivity.PAGE_LOGIN
                    }
                }
            }

        } else {
            mViewModel.toast.value = "密码修改失败"
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity).get(LoginViewModel::class.java)
        initEventListener()
    }

    companion object {
        fun newInstance(): ForgetFragment = ForgetFragment()
    }
}