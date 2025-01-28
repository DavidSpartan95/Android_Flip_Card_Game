package com.davidspartan.androidflipcardgame.model.realm

import com.davidspartan.androidflipcardgame.model.AllThemes
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class User: RealmObject {
    @PrimaryKey
    var id: ObjectId = BsonObjectId()
    var name: String = ""
    var score: Long = 0
    var themes: RealmList<Theme> = realmListOf()
    var selectedTheme: Theme? = AllThemes[0]

}