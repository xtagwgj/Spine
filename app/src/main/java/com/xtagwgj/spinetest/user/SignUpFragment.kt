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
import kotlinx.android.synthetic.main.fragment_signup.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 注册页面
 */
class SignUpFragment : Fragment(), View.OnClickListener {


    private lateinit var mViewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_signup, container, false)
    }

    private fun initView() {

        initEventListener()
    }

    private fun initEventListener() {
        bt_signUp.setOnClickListener(this)
        tv_loginNow.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            bt_signUp.id -> {
                //注册用户
                checkAndSignUp()
            }

            tv_loginNow.id -> {
                mViewModel.pageInt.value = LoginActivity.PAGE_LOGIN
            }
        }
    }

    /**
     * 检查注册参数
     *
     * 执行注册操作
     */
    private fun checkAndSignUp() {
        if (et_phone.isPhone().not()) {
            mViewModel.toast.value = "手机号格式错误"
            return
        }

        val phone = et_phone.text.toString().trim()
        val password = et_passwordConfirm.text.toString().trim()

        if (et_password.text.toString().trim() != password) {
            mViewModel.toast.value = "两次密码输入不一致"
            return
        }

        if (password.length < 4) {
            mViewModel.toast.value = "密码长度至少4位"
            return
        }

        //特定账号注册成功
        if (phone == "18566077938" && password == "111111") {
            mViewModel.toast.value = "用户注册成功"

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
            mViewModel.toast.value = "用户注册失败"
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity).get(LoginViewModel::class.java)
        initView()
    }


    companion object {
        fun newInstance(): SignUpFragment = SignUpFragment()
    }
}
