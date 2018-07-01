package com.yhishi.mytask

import android.app.Application
import io.realm.Realm


class MyTaskApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Reallm初期化
        Realm.init(this)
    }
}