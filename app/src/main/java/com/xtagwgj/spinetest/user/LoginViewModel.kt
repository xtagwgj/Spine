package com.xtagwgj.spinetest.user

import android.arch.lifecycle.MutableLiveData
import com.xtagwgj.base.mvvm.BaseViewModel

/**
 * 登录页面的 ViewModel
 * Created by xtagwgj on 2017/12/4.
 */
class LoginViewModel : BaseViewModel() {

    val pageInt = MutableLiveData<Int>().apply { value = 0 }


}