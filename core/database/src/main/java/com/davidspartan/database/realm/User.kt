package com.davidspartan.database.realm

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class User : RealmObject {
    @PrimaryKey
    var id: ObjectId = BsonObjectId()
    var name: String = ""
    var score: Long = 0
    var themes: RealmList<Theme> = realmListOf()

    private var _selectedTheme: Theme? = null  // Backing field stored in Realm

    var selectedTheme: Theme
        get() = _selectedTheme ?: themes.firstOrNull() ?: AllThemes[0] // Ensure it's never null
        set(value) {
            _selectedTheme = value
        }
}