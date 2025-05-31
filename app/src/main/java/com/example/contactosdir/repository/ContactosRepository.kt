package com.example.contactosdir.repository

import com.example.contactosdir.model.Contacto
import com.example.contactosdir.room.ContactosDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContactosRepository @Inject constructor(private val contactosDatabaseDao: ContactosDatabaseDao) {
    suspend fun addContacto(contacto: Contacto) = contactosDatabaseDao.insertContacto(contacto)
    suspend fun updateContacto(contacto: Contacto) = contactosDatabaseDao.updateContacto(contacto)
    suspend fun deleteContacto(contacto: Contacto) = contactosDatabaseDao.deleteContacto(contacto)
    fun getAllContactos(): Flow<List<Contacto>?> = contactosDatabaseDao.getAllContactos().flowOn(Dispatchers.IO).conflate()
    fun getContactoById(id: Int): Flow<Contacto?> = contactosDatabaseDao.getContactoById(id).flowOn(Dispatchers.IO).conflate()

}