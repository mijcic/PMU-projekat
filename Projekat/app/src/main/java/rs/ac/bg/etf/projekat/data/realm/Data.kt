package rs.ac.bg.etf.projekat.data.realm

import io.realm.kotlin.types.RealmObject


open class Person : RealmObject {
    var name: String = "Foo"
    var dog: Dog? = null
}

// Definisanje Dog modela
open class Dog : RealmObject {
    var name: String = ""
    var age: Int = 0
}