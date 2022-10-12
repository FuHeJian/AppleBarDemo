package com.example.applebardemo

import android.app.Application
import android.content.Context

class MyApplication:Application() {

    override fun getApplicationContext(): Context {
        return super.getApplicationContext().apply {
            GlobalContext.context = this
        }
    }
}