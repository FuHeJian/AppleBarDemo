package com.example.applebardemo

import android.content.Context

object GlobalContext {
    lateinit var context:Context
    lateinit var notificationContext:NotificationListen
    val notificationNullInfo = NotificationInfo(null,null)
}