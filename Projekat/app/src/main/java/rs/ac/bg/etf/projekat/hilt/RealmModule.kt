package rs.ac.bg.etf.projekat.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
/*
@Module
@InstallIn(SingletonComponent::class)
object RealmModule {

    @Provides
    fun provideRealm(): Realm {
        val configuration = RealmConfiguration.create(schema = setOf(Person::class, Dog::class))

        return Realm.open(configuration)
    }
}


@InstallIn(SingletonComponent::class)
@Module
object RealmModule {
    @Provides
    @Singleton
    fun provideRealm(
        @ApplicationContext context: Context,
    ): Realm {
        /*val realmConfig = RealmConfiguration.create(
            schema = setOf(
                Person::class, Dog::class
            ),
        )*/

        val config = RealmConfiguration. Builder(schema = setOf(
            Person::class, Dog::class
        )).initialData {

                val person = Person().apply {
                    name = "Carlo"
                    dog = Dog().apply { name = "Fido"; age = 16 }
                }
                copyToRealm(person, updatePolicy = UpdatePolicy.ALL)

        }
        return Realm.open(config.build())
    }
}*/