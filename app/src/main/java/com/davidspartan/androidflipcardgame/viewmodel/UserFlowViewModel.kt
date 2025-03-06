package com.davidspartan.androidflipcardgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidspartan.database.realm.MyRealm
import com.davidspartan.database.realm.User
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class UserFlowViewModel : ViewModel() {

    private val realm = MyRealm.realm

    val users = realm
        .query<User>()
        .asFlow()
        .map { results -> results.list.toList() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    // StateFlow for selected user ID
    private val selectedUserId = MutableStateFlow<ObjectId?>(null)

    val uiState: StateFlow<UserUiState> = combine(
        selectedUserId,
        users
    ) { selectedId, userList ->
        val selectedUser = userList.find { it.id == selectedId }
        if (selectedUser != null) {
            UserUiState.LoggedIn(selectedUser)
        } else {
            UserUiState.LoggedOut
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserUiState.LoggedOut
    )

    fun selectUser(user: User) {
        selectedUserId.value = user.id
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            realm.write {
                val deleteUser = query<User>("id == $0", user.id).find().first()
                delete(deleteUser)
            }
        }
    }

    fun addUser(name: String) {
        val defaultTheme = com.davidspartan.database.realm.Theme().apply {
            this.name = "Default"
            primaryHexColor = "#7b9acc"
            secondaryHexColor = "#FCF6F5"
            textHexColor = "#7b9acc"
            price = 0
        }

        val newUser = User().apply {
            this.name = name
            this.themes.add(defaultTheme)
            this.selectedTheme = defaultTheme
        }

        realm.writeBlocking {
            copyToRealm(newUser)
        }
    }
    fun userHasThemeWithName(user: User?, themeName: String): Boolean {
        if (user == null) return false
        user.themes.forEach { theme ->
            if (theme.name == themeName) {
                return true
            }
        }
        return false
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

    fun addScore(userId: ObjectId, score: Int) {
        viewModelScope.launch {
            realm.write {
                val user = query<com.davidspartan.database.realm.User>("id == $0", userId).find().first()
                user.score += score
            }
            selectedUserId.value = userId // Ensure UI state updates
        }
    }
}
sealed interface UserUiState {
    data class LoggedIn(val selectedUser: User) : UserUiState
    data object LoggedOut : UserUiState
}