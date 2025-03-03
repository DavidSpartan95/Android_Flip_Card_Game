package com.davidspartan.database.realm

import android.app.Application
import io.realm.kotlin.Realm

class MyRealm: Application() {

    companion object {
        lateinit var realm: Realm
    }

}