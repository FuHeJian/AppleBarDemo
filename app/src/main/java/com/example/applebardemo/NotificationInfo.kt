package com.example.applebardemo

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.ImageBitmap

data class NotificationInfo(var mAppInfo: ApplicationInfo?=null,var mIcon:ImageBitmap?=null,var mTitle:String="",var mContentStr:String="",var appName:String="")