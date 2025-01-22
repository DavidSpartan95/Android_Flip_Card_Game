package com.davidspartan.androidflipcardgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidspartan.androidflipcardgame.model.realm.MyApp
import com.davidspartan.androidflipcardgame.model.realm.Theme
import com.davidspartan.androidflipcardgame.model.realm.User
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser

    private val _selectedTheme = MutableStateFlow<Theme?>(null)
    val selectedTheme: StateFlow<Theme?> = _selectedTheme

    fun selectUser(user: User) {
        _selectedUser.value = user
        selectTheme(user.themes[0])

    }
    fun selectTheme(theme: Theme) {
        _selectedTheme.value = theme
    }

    fun addUser(name: String) {
        // Create a default theme
        val defaultTheme = Theme().apply {
            this.name = "Default"
            primaryHexColor = "#FFFFFF" // White background
            secondaryHexColor = "#000000" // Black secondary color
            textHexColor = "#FFFFFF" // White text color
        }

        // Create a new user with the default theme
        val newUser = User().apply {
            this.name = name
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

}