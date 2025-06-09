package rs.ac.bg.etf.projekat.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.data.realm.RealmSchemaProvider
import rs.ac.bg.etf.projekat.data.realm.realmClasses

@Module
@InstallIn(ViewModelComponent::class)
object RealmModule {

    @Provides
    @ViewModelScoped
    fun provideRealmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder(
            schema = RealmSchemaProvider.realmClasses
        )
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideRealm(config: RealmConfiguration): Realm {
        return Realm.open(config)
    }
}
