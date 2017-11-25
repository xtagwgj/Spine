package com.xtagwgj.spinetest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.xtagwgj.base.extensions.isPhone
import com.xtagwgj.base.utils.ToastUtil
import com.xtagwgj.base.view.citylist.CityInfoBean
import com.xtagwgj.base.view.citylist.CityListSelectActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        CityListSelectActivity.doAction(this)

        resources.getDrawable(R.mipmap.xtagwgj)

        ToastUtil.showToast(this, tv_show.text.toString() + " --> ${tv_show.isPhone()}")
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

                val (_, cityName, longitude, latitude) = bundle!!.getParcelable<Parcelable>("cityinfo") as CityInfoBean

                //城市名称
                //纬度
                //经度

                Log.e("MainActivity", "$cityName $longitude $latitude")
                ToastUtil.showToast(this, "$cityName $longitude $latitude")
            }
        }
    }
}
