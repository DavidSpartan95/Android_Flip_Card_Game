package com.davidspartan.androidflipcardgame.model.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class Theme: RealmObject {
    @PrimaryKey
    var id: ObjectId = BsonObjectId()
    var name: String = ""
    var primaryHexColor: String = ""
    var secondaryHexColor: String = ""
    var textHexColor: String = ""
}