package com.example.contactosdir.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactosdir.model.Contacto
import com.example.contactosdir.repository.ContactosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow // Importa StateFlow directamente
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch // Para manejar errores en el Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class ContactosViewModel @Inject constructor(
    private val repository: ContactosRepository
) : ViewModel() {

    // 1. Expone la lista de contactos como StateFlow. Este es el "estado" principal.
    private val _contactosList = MutableStateFlow<List<Contacto>>(emptyList())
    val contactosList: StateFlow<List<Contacto>> = _contactosList.asStateFlow()

    // Opcional: Para manejar errores de carga de la lista o mensajes a la UI
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadContactos()
    }

    private fun loadContactos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllContactos()
                .catch { exception ->
                    // Manejar errores de la carga del Flow
                    Log.e("ContactoViewModel", "Error al cargar contactos", exception)
                    _errorMessage.value = "Error al cargar contactos: ${exception.message}"
                    _contactosList.value = emptyList() // O mantener la lista anterior, según prefieras
                }
                .collect { listaDeContactos ->
                    // La comprobación isNullOrEmpty es una buena práctica aunque el Flow
                    // de Room usualmente emite una lista vacía en lugar de null.
                    if (listaDeContactos.isNullOrEmpty()) {
                        _contactosList.value = emptyList()
                    } else {
                        _contactosList.value = listaDeContactos
                    }
                    _errorMessage.value = null // Limpiar error si la carga fue exitosa
                }
        }
    }

    // 2. Funciones para interactuar con los contactos (añadir, actualizar, eliminar)
    // Estas funciones no modifican un "estado" complejo del ViewModel en sí,
    // sino que delegan la operación al repositorio. Los cambios en la base de datos
    // harán que el Flow de `getAllContactos()` emita una nueva lista, actualizando
    // `contactosList` automáticamente.

    fun addContacto(contacto: Contacto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addContacto(contacto)
                _errorMessage.value = null // Limpiar cualquier error previo
            } catch (e: Exception) {
                Log.e("ContactoViewModel", "Error al añadir contacto", e)
                _errorMessage.value = "Error al añadir contacto: ${e.message}"
            }
        }
    }

    fun updateContacto(contacto: Contacto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateContacto(contacto)
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e("ContactoViewModel", "Error al actualizar contacto", e)
                _errorMessage.value = "Error al actualizar contacto: ${e.message}"
            }
        }
    }

    fun deleteContacto(contacto: Contacto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteContacto(contacto)
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e("ContactoViewModel", "Error al eliminar contacto", e)
                _errorMessage.value = "Error al eliminar contacto: ${e.message}"
            }
        }
    }

    /**
     * Llama a esta función si necesitas recargar la lista manualmente,
     * aunque el Flow debería hacerlo automáticamente si los datos subyacentes cambian.
     */
    fun refreshContactos() {
        loadContactos()
    }
}