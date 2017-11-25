package com.xtagwgj.base

/**
 * Created by xtagwgj on 2017/11/25.
 */
interface BasePresenter {
    /**
     * 订阅任务
     */
    fun subscribe()

    /**
     * 清除数据，停止任务
     */
    fun unSubscribe()
}