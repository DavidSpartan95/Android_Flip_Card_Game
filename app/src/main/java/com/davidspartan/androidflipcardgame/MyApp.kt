package com.davidspartan.androidflipcardgame

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

import com.davidspartan.androidflipcardgame.viewmodel.GameViewModel
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel
import com.davidspartan.database.realm.MyRealm
import com.davidspartan.database.realm.Theme
import com.davidspartan.database.realm.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.module.dsl.singleOf


class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MyRealm.realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    User::class,
                    Theme::class,
                )
            )
        )
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}

val appModule = module {

    viewModelOf(::GameViewModel)
    singleOf(::UserRepositoryViewModel)


}