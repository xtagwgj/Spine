package com.xtagwgj.spinetest.main

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.xtagwgj.base.utils.ToastUtil
import com.xtagwgj.base.view.citylist.CityInfoBean
import com.xtagwgj.base.view.citylist.CityListSelectActivity
import com.xtagwgj.spinetest.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * 获取的数据
     */
    private lateinit var mainViewModel: MainViewModel

    companion object {
        fun doAction(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        //初始化 ViewModel
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        lifecycle.addObserver(mainViewModel)

        initView()
        initEventListener()
    }

    private fun initView() {
        //ViewModel 中城市信息的变化
        mainViewModel.cityCode.observe(this, Observer<String> {

            if (it == null) {
                tv_info.text = "未选择城市"
            } else {
                tv_info.text = "选择的城市 --> $it"
            }
        })
        //ViewModel 中金额信息的变化
        mainViewModel.money.observe(this, Observer<Float> {
            et_number.setContent("$it")
        })

        mainViewModel.selectCityEvent.observe(this, Observer<Void> {
            CityListSelectActivity.doAction(this)
        })

        mainViewModel.toast.observe(this, Observer<String> {
            it?.let {
                LogUtils.i(it)
                ToastUtil.showToast(this, it)
            }
        })

        mainViewModel.toast.value = "Test"
    }

    private fun initEventListener() {
        bt_city.setOnClickListener {
            mainViewModel.selectCityEvent.call()
        }
    }

    override fun onDestroy() {
        lifecycle.removeObserver(mainViewModel)
        super.onDestroy()
    }

    /**
     * 接收城市选择器选中的结果：
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CityListSelectActivity.CITY_SELECT_RESULT_FRAG) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return
                }
                val bundle = data.extras

                val selectInfo = bundle!!.getParcelable<Parcelable>("cityinfo") as CityInfoBean
                mainViewModel.selectCity.value = selectInfo
            }
        }
    }
}
