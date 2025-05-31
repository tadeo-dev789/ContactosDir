package com.example.contactosdir.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactosdir.model.Contacto // Importa tu modelo Contacto
import com.example.contactosdir.repository.ContactosRepository // Importa tu ContactosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
// Asegúrate de que la importación de 'collect' sea la correcta (kotlinx.coroutines.flow.collect)
// Si usas una versión anterior, podría ser collect{}. Si es más reciente, solo collect sin llaves.
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactoViewModel @Inject constructor(
    private val repository: ContactosRepository
) : ViewModel() {

    private val _contactosList = MutableStateFlow<List<Contacto>>(emptyList())
    val contactosList = _contactosList.asStateFlow()


    private val _selectedContacto = MutableStateFlow<Contacto?>(null)
    val selectedContacto = _selectedContacto.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllContactos().collect { listaDeContactos ->
                // La forma de tu CronosViewModel:
                if (listaDeContactos.isNullOrEmpty()) {
                    _contactosList.value = emptyList()
                } else {
                    _contactosList.value = listaDeContactos
                }

            }
        }
    }

    fun addContacto(contacto: Contacto) = viewModelScope.launch(Dispatchers.IO) {
        repository.addContacto(contacto)
    }

    fun updateContacto(contacto: Contacto) = viewModelScope.launch(Dispatchers.IO) { // Usar Dispatchers.IO
        repository.updateContacto(contacto)
    }

    fun deleteContacto(contacto: Contacto) = viewModelScope.launch(Dispatchers.IO) { // Usar Dispatchers.IO
        repository.deleteContacto(contacto)
    }

    fun clearSelectedContacto() {
        _selectedContacto.value = null
    }

    fun selectContactoById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val contacto = repository.getContactoById(id).first()
            _selectedContacto.value = contacto
        }
    }

}