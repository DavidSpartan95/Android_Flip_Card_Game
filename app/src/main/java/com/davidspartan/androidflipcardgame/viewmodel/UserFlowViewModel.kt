package com.davidspartan.androidflipcardgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidspartan.database.data.UserRepository
import com.davidspartan.database.realm.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class UserFlowViewModel(private val userRepository: UserRepository) : ViewModel() {

    val users = userRepository.users
    private val selectedUser = userRepository.selectedUser

    init {
        println("ViewModel initialized")
    }

    override fun onCleared() {
        super.onCleared()
        println("ViewModel cleared")
    }

    val uiState: StateFlow<UserUiState> = combine(
        selectedUser,
        users
    ) { selectedUser, userList ->
        // Make sure selectedUser is not null, then find the updated user in the list
        val updatedUser = userList.find { it.id == selectedUser?.id }
        println("Updating UI State: updatedUser=$updatedUser")
        updatedUser?.let { UserUiState.LoggedIn(it) } ?: UserUiState.LoggedOut
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserUiState.LoggedOut)

    fun selectUser(user: User) {
        userRepository.selectUser(user.id)
    }

    fun addUser(name: String) {
        viewModelScope.launch { userRepository.addUser(name) }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch { userRepository.deleteUser(user) }
    }

    fun addScore(userId: ObjectId, score: Int) {
        viewModelScope.launch { userRepository.addScore(userId, score) }
    }

    fun purchaseTheme(user: User, theme: com.davidspartan.database.realm.Theme): Boolean {
        viewModelScope.launch {  userRepository.purchaseTheme(user, theme) }
        return true
    }

    fun selectTheme(user: User, theme: com.davidspartan.database.realm.Theme) {
        viewModelScope.launch { userRepository.selectTheme(user, theme) }
    }
    fun userHasThemeWithName(user: User?, themeName: String): Boolean {
        if (user == null) return false
        return userRepository.userHasThemeWithName(user, themeName)
    }

}

sealed interface UserUiState {
    data class LoggedIn(val selectedUser: User) : UserUiState
    data object LoggedOut : UserUiState
}