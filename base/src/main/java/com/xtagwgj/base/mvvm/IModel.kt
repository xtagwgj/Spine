package com.xtagwgj.base.mvvm

/**
 * 从网络或是本地获取数据
 * Created by xtagwgj on 2017/12/3.
 */
interface IModel {

    /**
     * 销毁 网络数据的连接 及其他后续工作
     */
    fun onDestroy()
}