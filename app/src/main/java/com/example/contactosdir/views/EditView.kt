package com.example.contactosdir.views

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contactosdir.components.MainIconButton
import com.example.contactosdir.components.MainTitle
import com.example.contactosdir.model.Contacto
import com.example.contactosdir.viewModels.ContactosViewModel
import com.example.contactosdir.viewModels.ContactoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditView(
    navController: NavController,
    contactoVM: ContactosViewModel,
    contactosVM: ContactoViewModel,
    id: Int
) {
    val contacto by contactosVM.selectedContacto.collectAsState()

    LaunchedEffect(id) {
        contactosVM.selectContactoById(id)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "Editar Contacto") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { paddingValues ->
        contacto?.let { c ->
            ContentEditView(paddingValues, navController, contactoVM, contactosVM, c)
        } ?: run {
            Text(
                "Cargando...",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}

@Composable
fun ContentEditView(
    paddingValues: PaddingValues,
    navController: NavController,
    contactoVM: ContactosViewModel,
    contactosVM: ContactoViewModel,
    contacto: Contacto
) {
    var nombre by remember { mutableStateOf(contacto.nombre) }
    var apellidoPaterno by remember { mutableStateOf(contacto.apellidoPaterno) }
    var apellidoMaterno by remember { mutableStateOf(contacto.apellidoMaterno) }
    var correo by remember { mutableStateOf(contacto.correo) }
    var telefono by remember { mutableStateOf(contacto.telefono) }
    var domicilio by remember { mutableStateOf(contacto.domicilio) }


    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Nombre")
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = apellidoPaterno,
            onValueChange = { apellidoPaterno = it },
            label = { Text("Apellido Paterno") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Apellido Paterno")
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = apellidoMaterno,
            onValueChange = { apellidoMaterno = it },
            label = { Text("Apellido Materno") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Face, contentDescription = "Apellido Materno")
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Correo")
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Phone, contentDescription = "Teléfono")
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = domicilio,
            onValueChange = { domicilio = it },
            label = { Text("Domicilio") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Domicilio")
            },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && apellidoPaterno.isNotBlank() && telefono.isNotBlank()) {
                    val contactoEditado = Contacto(
                        id = contacto.id,
                        nombre = nombre.trim(),
                        apellidoPaterno = apellidoPaterno.trim(),
                        apellidoMaterno = apellidoMaterno.trim(),
                        correo = correo.trim(),
                        telefono = telefono.trim(),
                        domicilio = domicilio.trim(),

                    )
                    contactoVM.updateContacto(contactoEditado)
                    navController.popBackStack()
                } else {
                    // Puedes agregar aquí manejo de error si quieres
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar contacto")
        }
    }
}
