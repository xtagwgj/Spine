package com.xtagwgj.spinetest.main

import com.xtagwgj.base.BasePresenter
import com.xtagwgj.base.BaseView

/**
 * MainActivity 契约类
 * Created by xtagwgj on 2017/11/25.
 */
interface MainContract {

    interface Presenter : BasePresenter {

        fun dealData()

    }

    interface View : BaseView<Presenter> {
        fun showResult(msg: String)
    }

}