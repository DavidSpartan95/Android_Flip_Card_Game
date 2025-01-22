package com.davidspartan.androidflipcardgame.model.realm

import android.app.Application
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApp: Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    User::class,
                    Themes::class,
                )
            )
        )
    }
}