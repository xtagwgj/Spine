package com.xtagwgj.base.extensions

import android.widget.TextView
import com.blankj.utilcode.util.RegexUtils

/** 视图的扩展
 * Created by xtagwgj on 2017/11/25.
 */

//精确验证手机号
fun TextView.isPhone() = RegexUtils.isMobileExact(text.trim())

//精确验证身份证
fun TextView.isIdCard(): Boolean {
    val inputInfo = text.trim()
    return RegexUtils.isIDCard15(inputInfo) || RegexUtils.isIDCard18(inputInfo)
}

//精确验证邮箱
fun TextView.isEmail() = RegexUtils.isEmail(text.trim())