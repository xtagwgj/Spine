package com.xtagwgj.base.view

import android.os.CountDownTimer
import android.widget.TextView

/**
 * 倒计时工具
 * Created by xtagwgj on 2017/11/23.
 */

class TimerCount
/**
 * @param millisInFuture    倒计时时长
 * @param countDownInterval 倒计时间隔
 * @param timerView         进行倒计时的视图
 * @param finishText        倒计时结束时显示的文字
 * @param tickText          倒计时显示的文字  现在的格式时必须有 $d 来显示时间
 */
(millisInFuture: Long, countDownInterval: Long, private val timerView: TextView, private val finishText: String, private val tickText: String)
    : CountDownTimer(millisInFuture, countDownInterval) {

    init {
        setViewEnable(false)
    }

    override fun onTick(time: Long) {
        timerView.text = String.format(tickText, time / 1000)
    }

    override fun onFinish() {
        timerView.text = finishText
        setViewEnable(true)
    }


    private fun setViewEnable(enable: Boolean) {
        timerView.run {
            isClickable = enable
            isEnabled = enable
        }
    }

}
