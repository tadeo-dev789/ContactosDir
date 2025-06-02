package com.example.contactosdir.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contactos")
data class Contacto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val correo: String,
    val telefono: String,
    val domicilio: String = "",
    )