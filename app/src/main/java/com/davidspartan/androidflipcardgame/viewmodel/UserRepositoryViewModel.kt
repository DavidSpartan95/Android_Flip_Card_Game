package com.davidspartan.androidflipcardgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidspartan.database.realm.MyRealm
import com.davidspartan.database.realm.User
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class UserRepositoryViewModel : ViewModel() {

    private val realm = MyRealm.realm

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


    fun selectUser(user: User) {
        _selectedUser.value = user
    }

    fun selectTheme(user: com.davidspartan.database.realm.User, theme: com.davidspartan.database.realm.Theme) {
        if (!userHasThemeWithName(user, theme.name)) return

        viewModelScope.launch {
            realm.write {
                val queryUser = query<com.davidspartan.database.realm.User>("id == $0", user.id).find().first()
                queryUser.selectedTheme = theme
            }
            val updateUser = realm.query<com.davidspartan.database.realm.User>("id == $0", user.id).find().first()
            selectUser(updateUser)
        }
    }

    fun addThemeToUser(userId: ObjectId, theme: com.davidspartan.database.realm.Theme) {
        viewModelScope.launch {
            realm.write {
                val user = query<com.davidspartan.database.realm.User>("id == $0", userId).find().first()
                user.themes.add(theme)
            }
            val updateUser = realm.query<com.davidspartan.database.realm.User>("id == $0", userId).find().first()
            selectUser(updateUser)
        }
    }

    fun purchaseTheme(user: com.davidspartan.database.realm.User, theme: com.davidspartan.database.realm.Theme): Boolean {
        if (user.score < theme.price) return false

        viewModelScope.launch {
            realm.write {
                val queryUser = query<com.davidspartan.database.realm.User>("id == $0", user.id).find().first()
                queryUser.score -= theme.price
                queryUser.themes.add(theme)
            }
        }
        addThemeToUser(
            userId = user.id,
            theme = theme
        )
        return true
    }

    fun addUser(name: String) {
        // Create a default theme
        val defaultTheme = com.davidspartan.database.realm.Theme().apply {
            this.name = "Default"
            primaryHexColor = "#7b9acc"
            secondaryHexColor = "#FCF6F5"
            textHexColor = "#7b9acc"
            price = 0
        }

        // Create a new user with the default theme
        val newUser = com.davidspartan.database.realm.User().apply {
            this.name = name
            this.themes.add(defaultTheme)
            this.selectedTheme = defaultTheme
        }

        // Add the user and the default theme to the Realm database
        realm.writeBlocking {
            copyToRealm(newUser)
        }
    }

    fun userHasThemeWithName(user: com.davidspartan.database.realm.User?, themeName: String): Boolean {
        if (user == null) return false
        user.themes.forEach { theme ->
            if (theme.name == themeName) {
                return true
            }
        }
        return false
    }

    fun editUser(userId: ObjectId, newName: String) {
        viewModelScope.launch {
            realm.write {
                val user = query<com.davidspartan.database.realm.User>("id == $0", userId).find().first()
                user.name = newName
            }
        }
    }

    fun deleteUser(user: com.davidspartan.database.realm.User) {
        viewModelScope.launch {
            realm.write {
                val deleteUser = query<com.davidspartan.database.realm.User>("id == $0", user.id).find().first()
                delete(deleteUser)
            }
        }
    }

    fun addScore(userId: ObjectId, score: Int) {
        viewModelScope.launch {

            realm.write {
                val user = query<com.davidspartan.database.realm.User>("id == $0", userId).find().first()
                user.score += score
            }
            val updateUser = realm.query<com.davidspartan.database.realm.User>("id == $0", userId).find().first()
            selectUser(updateUser)
        }
    }
}