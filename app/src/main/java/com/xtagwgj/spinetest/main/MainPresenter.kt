package com.xtagwgj.spinetest.main

/**
 * MainActivity 的处理
 * Created by xtagwgj on 2017/11/25.
 */
class MainPresenter(var view: MainContract.View, private val viewModel: MainModel) : MainContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
        dealData()
    }

    override fun unSubscribe() {
        //activity 销毁的是否，会自动对数据进行清理，所以不需要手动清理
//        viewModel.selectCity.value = null
    }

    override fun dealData() {
        view.showResult("success")
    }
}