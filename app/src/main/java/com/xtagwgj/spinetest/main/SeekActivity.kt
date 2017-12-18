package com.xtagwgj.spinetest.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xtagwgj.spinetest.R

class SeekActivity : AppCompatActivity() {


    companion object {
        fun doAction(activity: Activity) {
            activity.startActivity(Intent(activity, SeekActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_seek)
        super.onCreate(savedInstanceState)
    }
}