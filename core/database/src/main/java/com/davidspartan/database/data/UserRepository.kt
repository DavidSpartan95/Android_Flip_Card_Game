package com.davidspartan.database.data

import com.davidspartan.database.realm.MyRealm
import com.davidspartan.database.realm.User
import com.davidspartan.database.realm.Theme
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.mongodb.kbson.ObjectId

class UserRepository() {

    private val realm = MyRealm.realm

    private val usersFlow: Flow<List<User>> = realm
        .query<User>()
        .asFlow()
        .map { it.list.toList() }
        .onEach { println("UserRepository emitting users: $it") } // Debugging

    val users: StateFlow<List<User>> = usersFlow
        .stateIn(
            CoroutineScope(Dispatchers.IO + SupervisorJob()), // Avoid memory leaks
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )
    var selectedUser = MutableStateFlow<User?>(null)

    fun selectUser(userId: ObjectId) {
        selectedUser.value = realm.query<User>("id == $0", userId).first().find()
        println("Selected user: ${selectedUser.value?.name}")
    }
    suspend fun addUser(name: String) {
        realm.write {
            val defaultTheme = Theme().apply {
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

            copyToRealm(newUser)
        }
    }

    suspend fun deleteUser(user: User) {
        println("Deleting user: $user")
        realm.write {
            val deleteUser = query<User>("id == $0", user.id).find().first()
            delete(deleteUser)
        }
    }

    suspend fun addScore(userId: ObjectId, score: Int) {
        realm.write {
            val user = query<User>("id == $0", userId).find().first()
            user.score += score
        }
    }

    suspend fun purchaseTheme(user: User, theme: Theme): Boolean {
        if (user.score < theme.price) return false

        realm.write {
            val queryUser = query<User>("id == $0", user.id).find().first()
            queryUser.score -= theme.price
            queryUser.themes.add(theme)
        }
        return true
    }

    suspend fun selectTheme(user: User, theme: Theme) {
        if (!userHasThemeWithName(user, theme.name)) return

        realm.write {
            val queryUser = query<User>("id == $0", user.id).find().first()
            queryUser.selectedTheme = theme
        }
        selectUser(user.id)
    }

    fun userHasThemeWithName(user: User?, themeName: String): Boolean {
        if (user == null) return false
        return user.themes.any { it.name == themeName }
    }
}

