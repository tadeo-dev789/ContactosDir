package com.example.contactosdir.di

import android.content.Context
import androidx.room.Room
import com.example.contactosdir.room.ContactosDatabase
import com.example.contactosdir.room.ContactosDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideContactosDatabase(@ApplicationContext context: Context): ContactosDatabase {
        return Room.databaseBuilder(
            context,
            ContactosDatabase::class.java,
            "contactos_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideContactosDao(contactosDatabase: ContactosDatabase): ContactosDatabaseDao {
        return contactosDatabase.contactosDatabaseDao()
    }
}
