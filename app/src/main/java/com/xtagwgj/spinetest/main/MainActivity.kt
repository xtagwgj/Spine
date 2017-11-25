package com.xtagwgj.spinetest.main

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.xtagwgj.base.view.citylist.CityInfoBean
import com.xtagwgj.base.view.citylist.CityListSelectActivity
import com.xtagwgj.spinetest.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {

    /**
     * 数据处理的中心
     */
    private lateinit var presenter: MainContract.Presenter
    private lateinit var mainModel: MainModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        mainModel = ViewModelProviders.of(this).get(MainModel::class.java)
        mainModel.selectCity.observe(this, Observer<CityInfoBean> {

            if (it == null) {
                tv_info.text = "未选择城市"
            } else {
                tv_info.text = "选择的城市 --> ${it.name} ${it.longitude} ${it.latitude}"
            }
        })
        MainPresenter(this, mainModel)

        bt_city.setOnClickListener {
            CityListSelectActivity.doAction(this)
        }
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = checkNotNull(presenter)
    }

    override fun showResult(msg: String) {
        LogUtils.e(mainModel.selectCity.value.toString())
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }


    override fun onDestroy() {
        presenter.unSubscribe()
        super.onDestroy()
    }

    //接收选择器选中的结果：
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CityListSelectActivity.CITY_SELECT_RESULT_FRAG) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return
                }
                val bundle = data.extras

                val selectInfo = bundle!!.getParcelable<Parcelable>("cityinfo") as CityInfoBean
                mainModel.selectCity.value = selectInfo

                //城市名称
                //纬度
                //经度
//                val (_, cityName, longitude, latitude) = bundle!!.getParcelable<Parcelable>("cityinfo") as CityInfoBean
//                Log.e("MainActivity", "$cityName $longitude $latitude")
//                ToastUtil.showToast(this, "$cityName $longitude $latitude")
            }
        }
    }
}
