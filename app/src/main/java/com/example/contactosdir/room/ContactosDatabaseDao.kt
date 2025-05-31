package com.example.contactosdir.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.contactosdir.model.Contacto // Asegúrate de que la ruta sea correcta
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactosDatabaseDao {

    @Query("SELECT * FROM contactos")
    fun getAllContactos(): Flow<List<Contacto>>
    @Query("SELECT * FROM contactos WHERE id = :id")
    fun getContactoById(id: Int): Flow<Contacto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacto(contacto: Contacto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateContacto(contacto: Contacto)

    @Delete
    suspend fun deleteContacto(contacto: Contacto)
}
