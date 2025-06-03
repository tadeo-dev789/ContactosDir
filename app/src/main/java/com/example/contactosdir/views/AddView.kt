package com.example.contactosdir.views

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.contactosdir.R
import com.example.contactosdir.components.MainIconButton
import com.example.contactosdir.components.MainTitle
import com.example.contactosdir.model.Contacto
import com.example.contactosdir.viewModels.ContactosViewModel
import com.example.contactosdir.viewModels.ContactoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddView(
    navController: NavController,
    contactoVM: ContactosViewModel,
    contactosVM: ContactoViewModel
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "Agregar Contacto") },
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
    ) {
        ContentAddView(it, navController, contactoVM, contactosVM)
    }
}

@Composable
fun ContentAddView(
    paddingValues: PaddingValues,
    navController: NavController,
    contactoVM: ContactosViewModel,
    contactosVM: ContactoViewModel
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri // Guardas la imagen seleccionada
    }

    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var domicilio by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen de perfil con botón de editar
        Box(modifier = Modifier.size(120.dp)) {
            Image(
                painter = if (imageUri != null)
                    rememberAsyncImagePainter(imageUri)
                else
                    painterResource(id = R.drawable.usuario),
                contentDescription = "Foto de contacto",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            )

            IconButton(
                onClick = {
                    launcher.launch("image/*")
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar imagen",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = apellidoPaterno,
            onValueChange = { apellidoPaterno = it },
            label = { Text("Apellido Paterno") },
            leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = apellidoMaterno,
            onValueChange = { apellidoMaterno = it },
            label = { Text("Apellido Materno") },
            leadingIcon = { Icon(Icons.Default.Face, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = domicilio,
            onValueChange = { domicilio = it },
            label = { Text("Domicilio") },
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && apellidoPaterno.isNotBlank() && telefono.isNotBlank()) {
                    val rutaLocal = imageUri?.let { guardarImagenEnInterno(context, it) } ?: ""
                    val nuevoContacto = Contacto(

                        nombre = nombre.trim(),
                        apellidoPaterno = apellidoPaterno.trim(),
                        apellidoMaterno = apellidoMaterno.trim(),
                        correo = correo.trim().toLowerCase(),
                        telefono = telefono.trim(),
                        domicilio = domicilio.trim(),
                        fotoUri = rutaLocal
                    )
                    contactoVM.addContacto(nuevoContacto)
                    navController.popBackStack()
                } else {
                    // Mostrar error si deseas
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar contacto")
        }
    }
}


