package com.xtagwgj.base.view.citylist.sortlistview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

import com.xtagwgj.base.R


class SideBar : View {
    // 触摸事件
    private var onTouchingLetterChangedListener: OnTouchingLetterChangedListener? = null
    private var choose = -1// 选中
    private val paint = Paint()

    private var mTextDialog: TextView? = null

    fun setTextView(mTextDialog: TextView) {
        this.mTextDialog = mTextDialog
    }


    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    /**
     * 重写这个方法
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 获取焦点改变背景颜色.
        val height = height// 获取对应高度
        val width = width // 获取对应宽度
        val singleHeight = height / b.size// 获取每一个字母的高度

        for (i in b.indices) {
            paint.color = Color.rgb(33, 65, 98)
            // paint.setColor(Color.WHITE);
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.isAntiAlias = true
            paint.textSize = 20f
            // 选中的状态
            if (i == choose) {
                paint.color = Color.parseColor("#000000")
                paint.isFakeBoldText = true
            }
            // x坐标等于中间-字符串宽度的一半.
            val xPos = width / 2 - paint.measureText(b[i]) / 2
            val yPos = (singleHeight * i + singleHeight).toFloat()
            canvas.drawText(b[i], xPos, yPos, paint)
            paint.reset()// 重置画笔
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val y = event.y// 点击y坐标
        val oldChoose = choose
        val listener = onTouchingLetterChangedListener
        val c = (y / height * b.size).toInt()// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        when (action) {
            MotionEvent.ACTION_UP -> {
                setBackgroundDrawable(ColorDrawable(0x00000000))
                choose = -1//
                invalidate()
                if (mTextDialog != null) {
                    mTextDialog!!.visibility = View.INVISIBLE
                }
            }

            else -> {
                setBackgroundResource(R.drawable.sidebar_background)
                if (oldChoose != c) {
                    if (c >= 0 && c < b.size) {
                        listener?.onTouchingLetterChanged(b[c])
                        if (mTextDialog != null) {
                            mTextDialog!!.text = b[c]
                            mTextDialog!!.visibility = View.VISIBLE
                        }

                        choose = c
                        invalidate()
                    }
                }
            }
        }
        return true
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    fun setOnTouchingLetterChangedListener(
            onTouchingLetterChangedListener: OnTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener
    }

    /**
     * 接口
     *
     * @author coder
     */
    interface OnTouchingLetterChangedListener {
        fun onTouchingLetterChanged(s: String)
    }

    companion object {
        // 26个字母
        var b = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#")
    }

}