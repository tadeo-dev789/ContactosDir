package com.example.contactosdir.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contactosdir.model.Contacto


@Database(
    entities = [Contacto::class],
    version = 1,
    exportSchema = false
)
abstract class ContactosDatabase : RoomDatabase() {

    abstract fun contactosDatabaseDao(): ContactosDatabaseDao

}