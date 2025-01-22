package com.davidspartan.androidflipcardgame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidspartan.androidflipcardgame.model.realm.MyApp
import com.davidspartan.androidflipcardgame.model.realm.Themes
import com.davidspartan.androidflipcardgame.model.realm.User
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class UserRepositoryViewModel :ViewModel() {

    private val realm = MyApp.realm

    val users = realm
        .query<User>()
        .asFlow()
        .map { results ->
            results.list.toList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun addUser() {
        // Create a default theme
        val defaultTheme = Themes().apply {
            name = "Default Theme"
            primaryHexColor = "#FFFFFF" // White background
            secondaryHexColor = "#000000" // Black secondary color
            textHexColor = "#FFFFFF" // White text color
        }

        // Create a new user with the default theme
        val newUser = User().apply {
            this.name = "DavidSpartan"
            this.themes.add(defaultTheme)
        }

        // Add the user and the default theme to the Realm database
        realm.writeBlocking {
            copyToRealm(newUser)
        }
    }

    fun editUser(userId: ObjectId, newName: String, incrementScore: Boolean) {
        viewModelScope.launch {
            realm.write {
                val user = query<User>("id == $0", userId).find().first()
                user.name = "Michigan"
                user.score++
            }
        }
    }

    private val _users = MutableLiveData<List<User>>()

}