package com.example.applebardemo

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern

class FloatingBarService:Service() {
    val keyboardViewLifecycleOwner = KeyboardViewLifecycleOwner()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        keyboardViewLifecycleOwner.onCreate()
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStartCommand(intent: Intent?, flagss: Int, startId: Int): Int {
        super.onStartCommand(intent, flagss, startId)
        keyboardViewLifecycleOwner.onResume()
        val lp = WindowManager.LayoutParams().apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
            //设置背景透明，否则有白色矩形框
            format = PixelFormat.TRANSPARENT
        }
        val windowManager = getSystemService(WindowManager::class.java)
        val lview = ComposeView(this.applicationContext)
        lview.setContent {
            floatingBar()
        }
        keyboardViewLifecycleOwner.attachToDecorView(lview)
        windowManager.addView(lview,lp)
        return START_STICKY
    }
}