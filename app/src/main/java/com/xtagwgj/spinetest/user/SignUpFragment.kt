package com.xtagwgj.spinetest.user

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xtagwgj.spinetest.R

/**
 * 注册页面
 */
class SignUpFragment : Fragment() {

    private lateinit var mViewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_signup, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }


    companion object {
        fun newInstance(): SignUpFragment = SignUpFragment()
    }
}