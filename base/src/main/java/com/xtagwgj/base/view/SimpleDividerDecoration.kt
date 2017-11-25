package com.xtagwgj.base.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.blankj.utilcode.util.ConvertUtils


/**
 * recyclerView的分割线
 * Created by xtagwgj on 2017/7/21.
 */
class SimpleDividerDecoration(context: Context,
                              private var dividerHeight: Int = ConvertUtils.dp2px(1F),
                              dividerColorInt: Int = Color.parseColor("#f7f7f7"),
                              private var leftPadding: Int = 0) : RecyclerView.ItemDecoration() {

    private val dividerPaint: Paint = Paint()

    init {
        dividerPaint.color = dividerColorInt
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val left = parent.paddingLeft + leftPadding
        val right = parent.width - parent.paddingRight

        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom
            val bottom = view.bottom + dividerHeight
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), dividerPaint)
        }
    }

}