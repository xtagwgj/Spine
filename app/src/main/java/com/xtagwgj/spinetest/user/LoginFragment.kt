package com.xtagwgj.spinetest.user

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.SPUtils
import com.jakewharton.rxbinding2.widget.RxTextView
import com.xtagwgj.base.view.TimerCount
import com.xtagwgj.base.view.loadingdialog.RxDialogLoading
import com.xtagwgj.spinetest.R
import com.xtagwgj.spinetest.main.MainActivity
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 登录页面
 */
class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var mViewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_login, container, false)
        return rootView
    }

    private fun initView() {

    }

    private fun initEventListener() {

        et_phone?.let {
            //根据手机号来判断是否可点击获取验证码
            RxTextView.textChanges(et_phone)
                    .map { RegexUtils.isMobileExact(it) }
                    .subscribe { bt_captcha.isEnabled = it }
        }


        if (et_phone != null && et_captcha != null) {
            //根据手机号和输入的验证码来判断是否可点击登录按钮
            Observable.combineLatest(
                    RxTextView.textChanges(et_phone).map { bt_captcha.isEnabled },
                    RxTextView.textChanges(et_captcha).map { it.length >= 6 },
                    BiFunction<Boolean, Boolean, Boolean> { phoneValid, codeValid -> phoneValid && codeValid }
            )
                    .subscribe { bt_login.isEnabled = it }
        }

        bt_captcha.setOnClickListener(this)
        bt_login.setOnClickListener(this)
        tv_forgetPwd.setOnClickListener(this)
        tv_signUp.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            bt_captcha.id -> {
                //获取验证码
                TimerCount(60000, 1000, bt_captcha,
                        "获取验证码", "%ds 后重试").start()
            }

            bt_login.id -> {
                //登录
                doActionLogin()
            }

            tv_forgetPwd.id -> {
                mViewModel.pageInt.value = LoginActivity.PAGE_FORGET
            }

            tv_signUp.id -> {
                mViewModel.pageInt.value = LoginActivity.PAGE_SIGNUP
            }

            else -> {

            }
        }

    }

    private fun doActionLogin() {
        val phone = et_phone.text.toString().trim()
        val code = et_captcha.text.toString().trim()
        val isRemember = false

        if (phone == "18566077938" && code == "111111") {

            //显示加载对话框
            val dialog = RxDialogLoading(activity).apply {
                setCanceledOnTouchOutside(false)
                show()
            }

            SPUtils.getInstance().apply {
                put("isRememberPassword", isRemember)

                if (isRemember) {
                    put("phone", phone)
                    //假如code是密码
                    put("password", code)
                } else {
                    remove("phone")
                    remove("password")
                }
            }

            doAsync {
                SystemClock.sleep(3000)
                uiThread {
                    dialog.dismiss()
                    MainActivity.doAction(activity)
                }
            }

        } else {
            mViewModel.toast.value = "验证码输入不正确"
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity).get(LoginViewModel::class.java)

        mViewModel.phone.observe(this, Observer { phone ->
            et_phone.run {
                setText(phone)
                setSelection(phone?.length ?: 0)
            }
        })

        mViewModel.password.observe(this, Observer { password ->
            et_captcha.run {
                setText(password)
                setSelection(password?.length ?: 0)
            }

        })

        mViewModel.tryLogin.observe(this, Observer { autoLogin ->
            if (autoLogin == true) {
                bt_login.performClick()
            }
        })

        initView()
        initEventListener()
    }


    companion object {
        fun newInstance(): LoginFragment = LoginFragment()
    }
}