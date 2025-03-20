package rs.ac.bg.etf.projekat.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.MainActivity
import rs.ac.bg.etf.projekat.data.realm.Dog
import rs.ac.bg.etf.projekat.data.realm.Person

/*
@HiltViewModel
class MyViewModel2(
    //private val realm: Realm // Realm je injektovan pomoću Hilt-a
) : ViewModel() {

    init {
        addInitialData()
    }

    private fun addInitialData() {
        viewModelScope.launch {
            // Korišćenje Realm za upisivanje podataka u bazu
            realm.write {
                val person = Person().apply {
                    name = "Carlo"
                    dog = Dog().apply { name = "Fido"; age = 16 }
                }
                copyToRealm(person, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    suspend fun createPerson() {
        val person = Person().apply {
            name = "Carlo"
            dog = Dog().apply { name = "Fido"; age = 16 }
        }
        realm.write {
            copyToRealm(person)
        }
    }


}*/