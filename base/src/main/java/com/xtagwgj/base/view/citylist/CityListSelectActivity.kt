package com.xtagwgj.base.view.citylist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import com.xtagwgj.base.R
import com.xtagwgj.base.view.CleanableEditView
import com.xtagwgj.base.view.citylist.sortlistview.*
import java.util.*

/**
 * 城市选择
 */
/*
 //接收选择器选中的结果：
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CityListSelectActivity.CITY_SELECT_RESULT_FRAG) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return
                }
                val bundle = data.extras

                val (_, cityName, longitude, latitude) = bundle!!.getParcelable<Parcelable>("cityinfo") as CityInfoBean ?: return

                //城市名称
                //纬度
                //经度

                Log.e("MainActivity", "$cityName $longitude $latitude")
            }
        }
    }
 */
class CityListSelectActivity : AppCompatActivity() {

    lateinit var mCityTextSearch: CleanableEditView
    lateinit var mCurrentCityTag: TextView
    lateinit var mCurrentCity: TextView
    lateinit var mLocalCityTag: TextView
    lateinit var mLocalCity: TextView
    lateinit var sortListView: ListView
    lateinit var mDialog: TextView
    lateinit var mSidrbar: SideBar

    lateinit var adapter: SortAdapter

    /**
     * 汉字转换成拼音的类
     */
    private var sourceDateList: MutableList<SortModel>? = null

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private var pinyinComparator: PinyinComparator? = null
    private var cityListInfo: List<CityInfoBean> = ArrayList()

    private var cityInfoBean: CityInfoBean? = CityInfoBean()

    companion object {
        val CITY_SELECT_RESULT_FRAG = 0x0000032

        fun doAction(activity: Activity) {
            val intent = Intent(activity, CityListSelectActivity::class.java)
            activity.startActivityForResult(intent, CityListSelectActivity.CITY_SELECT_RESULT_FRAG)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_city_list_select)
        super.onCreate(savedInstanceState)

        initView()
        initList()

        setCityData(CityUtils.getCityList())
    }

    private fun initView() {
        mCityTextSearch = findViewById<View>(R.id.cityInputText) as CleanableEditView
        mCurrentCityTag = findViewById<View>(R.id.currentCityTag) as TextView
        mCurrentCity = findViewById<View>(R.id.currentCity) as TextView
        mLocalCityTag = findViewById<View>(R.id.localCityTag) as TextView
        mLocalCity = findViewById<View>(R.id.localCity) as TextView
        sortListView = findViewById<View>(R.id.country_lvcountry) as ListView
        mDialog = findViewById<View>(R.id.dialog) as TextView
        mSidrbar = findViewById(R.id.sidrbar)
    }


    private fun setCityData(cityList: List<CityInfoBean>) {
        cityListInfo = cityList
        val count = cityList.size
        val list = (0 until count).map { cityList[it].name }

        sourceDateList!!.addAll(filledData(list.toTypedArray()))
        // 根据a-z进行排序源数据
        Collections.sort(sourceDateList!!, pinyinComparator)
        adapter.notifyDataSetChanged()
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private fun filledData(date: Array<String>): List<SortModel> {
        val mSortList = ArrayList<SortModel>()

        for (i in date.indices) {
            val sortModel = SortModel()
            sortModel.name = date[i]
            //汉字转换成拼音
            val pinyin = CharacterParser.getSelling(date[i])
            val sortString = pinyin.substring(0, 1).toUpperCase()

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]".toRegex())) {
                sortModel.sortLetters = sortString.toUpperCase()
            } else {
                sortModel.sortLetters = "#"
            }

            mSortList.add(sortModel)
        }
        return mSortList
    }


    private fun initList() {
        sourceDateList = ArrayList()
        adapter = SortAdapter(this@CityListSelectActivity, sourceDateList!!)
        sortListView.adapter = adapter

        //实例化汉字转拼音类
        pinyinComparator = PinyinComparator()
        mSidrbar.setTextView(mDialog)
        //设置右侧触摸监听
        mSidrbar.setOnTouchingLetterChangedListener(object : SideBar.OnTouchingLetterChangedListener {

            override fun onTouchingLetterChanged(s: String) {
                //该字母首次出现的位置
                val position = adapter.getPositionForSection(s[0].toInt())
                if (position != -1) {
                    sortListView.setSelection(position)
                }
            }
        })

        sortListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val cityName = (adapter.getItem(position) as SortModel).name
            cityInfoBean = cityInfoBean?.findCity(cityListInfo, cityName!!)
            val intent = Intent()
            val bundle = Bundle()
            bundle.putParcelable("cityinfo", cityInfoBean)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        //根据输入框输入值的改变来过滤搜索
        mCityTextSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private fun filterData(filterStr: String) {
        var filterDateList: MutableList<SortModel>? = ArrayList()

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = sourceDateList
        } else {
            filterDateList!!.clear()
            for (sortModel in sourceDateList!!) {
                val name = sortModel.name
                if (name!!.contains(filterStr) || CharacterParser.getSelling(name).startsWith(filterStr)) {
                    filterDateList.add(sortModel)
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList!!, pinyinComparator)
        adapter.updateListView(filterDateList)
    }


}
