package com.xtagwgj.base.utils

import android.text.format.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * 时间的工具类
 * Created by xtagwgj on 2017/11/18.
 */
class TimeUtils {

    val ONE_MINUTE_MILLIONS = 60000.toLong()

    val ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS

    var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    /**
     * 得到剩余天数
     *
     * @param endTime 结束时间
     * @return
     */
    fun getDayLast(endTime: String): Int {
        try {
            val nowTime = Date().time
            val lastTime = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(endTime).time

            val distance = lastTime - nowTime
            if (distance <= 0) {
                //如果是小于或等于0，则为0
                return 0
            }

            val rate = (distance / (1.0f * 24f * 3600f * 1000f)).toDouble()
            return (rate + 0.5f).toInt()
        } catch (e: ParseException) {
            e.printStackTrace()
            return -1
        }
    }

    /**
     * 获取短时间格式
     *
     * @return
     */
    fun getShortTime(millis: Long): String {
        val date = Date(millis)
        val curDate = Date()

        val durTime = curDate.time - date.time

        val dayStatus = calculateDayStatus(date, Date())

        return if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
            "刚刚"
        } else if (durTime < ONE_HOUR_MILLIONS) {
            (durTime / ONE_MINUTE_MILLIONS).toString() + "分钟前"
        } else if (dayStatus == 0) {
            (durTime / ONE_HOUR_MILLIONS).toString() + "小时前"
        } else if (dayStatus == -1) {
            "昨天" + DateFormat.format("HH:mm", date)
        } else if (isSameYear(date, curDate) && dayStatus < -1) {
            DateFormat.format("MM-dd", date).toString()
        } else {
            DateFormat.format("yyyy-MM", date).toString()
        }
    }

    /**
     * 判断是否是同一年
     * @param targetTime
     * @param compareTime
     * @return
     */
    fun isSameYear(targetTime: Date, compareTime: Date): Boolean {
        val tarCalendar = Calendar.getInstance()
        tarCalendar.time = targetTime
        val tarYear = tarCalendar.get(Calendar.YEAR)

        val compareCalendar = Calendar.getInstance()
        compareCalendar.time = compareTime
        val comYear = compareCalendar.get(Calendar.YEAR)

        return tarYear == comYear
    }

    /**
     * 判断是否处于今天还是昨天，0表示今天，-1表示昨天，小于-1则是昨天以前
     * @param targetTime
     * @param compareTime
     * @return
     */
    fun calculateDayStatus(targetTime: Date, compareTime: Date): Int {
        val tarCalendar = Calendar.getInstance()
        tarCalendar.time = targetTime
        val tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR)

        val compareCalendar = Calendar.getInstance()
        compareCalendar.time = compareTime
        val comDayOfYear = compareCalendar.get(Calendar.DAY_OF_YEAR)

        return tarDayOfYear - comDayOfYear
    }

    /**
     * 将秒数转换成00:00的字符串，如 118秒 -> 01:58
     * @param time
     * @return
     */
    fun secToTime(time: Int): String {
        var timeStr: String?

        if (time <= 0)
            return "00:00"
        else {
            var minute = time / 60
            if (minute < 60) {
                val second = time % 60
                timeStr = unitFormat(minute) + ":" + unitFormat(second)
            } else {
                val hour = minute / 60
                if (hour > 99)
                    return "99:59:59"
                minute %= 60
                val second = time - hour * 3600 - minute * 60
                timeStr = (unitFormat(hour) + ":" + unitFormat(minute) + ":"
                        + unitFormat(second))
            }
        }
        return timeStr
    }

    fun unitFormat(i: Int): String {
        return if (i in 0..9)
            "0" + Integer.toString(i)
        else
            "" + i
    }


    /**
     * 获取当前月1号  返回格式yyyy-MM-dd (eg: 2007-06-01)
     *
     * @return
     */
    fun getMonthBegin(): String {
        val yearMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
        return yearMonth + "-01"
    }


    /**
     * 与当前时间对比
     * @param time
     * @return
     */
    fun compareTime(time: String): Long {
        var timeLong: Long = 0
        var curTimeLong: Long = 0

        try {
            timeLong = sdf.parse(time).time
            curTimeLong = sdf.parse(getCurrentTimeString())
                    .time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        // 当前时间减去传入的时间
        return curTimeLong - timeLong
    }

    /**
     * 返回yyyy-MM-dd HH:mm:ss类型的时间字符串
     */
    fun getCurrentTimeString(): String = sdf.format(Date())

    /**
     * @param milliseconds 时间值
     * @param isDetail 是否需要显示具体时间段 + 时分和星期 + 时分
     * @return
     */
    fun getTimeShowString(milliseconds: Long, isDetail: Boolean): String {

        val currentTime = Date(milliseconds)
        val today = Date()
        val todayStart = Calendar.getInstance()
        todayStart.set(Calendar.HOUR_OF_DAY, 0)
        todayStart.set(Calendar.MINUTE, 0)
        todayStart.set(Calendar.SECOND, 0)
        todayStart.set(Calendar.MILLISECOND, 0)
        val todaybegin = todayStart.time
        val yesterdaybegin = Date(todaybegin.time - 3600 * 24 * 1000)
        val preyesterday = Date(
                yesterdaybegin.time - 3600 * 24 * 1000)

        val dataString = if (!currentTime.before(todaybegin)) {
            "今天"
        } else if (!currentTime.before(yesterdaybegin)) {
            "昨天"
        } else if (!currentTime.before(preyesterday)) {
            "前天"
        } else if (isSameWeekDates(currentTime, today)) {
            getWeekOfDate(currentTime)
        } else {
            val dateformatter = SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault())
            dateformatter.format(currentTime)
        }

        val timeformatter24 = SimpleDateFormat("HH:mm",
                Locale.getDefault())
        val timeStringBy24 = timeformatter24.format(currentTime)

        return if (isDetail) {//显示具体的时间
            //在聊天界面显示时间，如果是今天则显示当前时间段加上时和分  如上午 9:58
            if (!currentTime.before(todaybegin)) {//如果是今天
                getTodayTimeBucket(currentTime)//根据时间段分为凌晨 上午 下午等
            } else {
                dataString + " " + timeStringBy24//如果是昨天 则是 昨天 9：58 如果是同在一个星期，前天之前的时间则显示 星期一 9：58
            }
        } else {
            //在会话记录界面不需要展示很具体的时间
            if (!currentTime.before(todaybegin)) {//如果是今天
                timeStringBy24//直接返回时和分 如 9:58
            } else {
                dataString//如果不是今天，不需要展示时和分 如 昨天 前天 星期一
            }
        }
    }


    /**
     * 判断两个日期是否在同一周
     *
     * @param date1
     * @param date2
     * @return
     */
    fun isSameWeekDates(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        val subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) === cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) === cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) === cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true
        }
        return false
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    fun getWeekOfDate(date: Date): String {
        val weekDaysName = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        // String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        val calendar = Calendar.getInstance()
        calendar.time = date
        val intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        return weekDaysName[intWeek]
    }

    /**
     * 根据不同时间段，显示不同时间
     *
     * @param date
     * @return
     */
    fun getTodayTimeBucket(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val timeformatter0to11 = SimpleDateFormat("KK:mm",
                Locale.getDefault())
        val timeformatter1to12 = SimpleDateFormat("hh:mm",
                Locale.getDefault())
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 0..4 -> return "凌晨 " + timeformatter0to11.format(date)
            in 5..11 -> return "上午 " + timeformatter0to11.format(date)
            in 12..17 -> return "下午 " + timeformatter1to12.format(date)
            in 18..23 -> return "晚上 " + timeformatter1to12.format(date)
            else -> return ""
        }
    }

    /**
     * 获取当前时间 yyyy-MM-dd格式
     *
     * @return
     */
    fun getCurrentTimeYMD(): String =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    /**
     * 将yyyy-MM-dd的字符串转换成Date对象
     */
    fun getDateByYMD(time: String): Date? {
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }
}