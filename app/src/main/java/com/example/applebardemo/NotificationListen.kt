package com.example.applebardemo

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Icon
import android.media.session.MediaSession
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

class NotificationListen:NotificationListenerService() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.notificationContext = this
    }

    override fun onStartCommand(intent: Intent?, flagss: Int, startId: Int): Int {
        println("服务被启动了")

        return super.onStartCommand(intent, flagss, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object{
        var notificationInfo = mutableStateOf(NotificationInfo(null,null))
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val notificaion = sbn?.notification
        val extras = sbn?.notification?.extras

        if(notificaion!=null)
        {
            extras?.let {
                extras.keySet().forEach {
                    println(it+"-----"+extras.get(it).toString())
                }
                val app = extras.get("android.appInfo") as ApplicationInfo?
                val notificationIcon = extras.get("android.largeIcon") as Icon?

                val appContext = createPackageContext(app?.packageName, Context.CONTEXT_RESTRICTED)
                notificationInfo.value = NotificationInfo(app,
                    app?.loadIcon(appContext.packageManager)?.toBitmap()?.asImageBitmap(),
                    extras.getString(Notification.EXTRA_TITLE,""),
                    extras.getString(Notification.EXTRA_TEXT,""),
                    app?.loadLabel(appContext.packageManager).toString()
                )
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

}