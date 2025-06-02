package com.example.contactosdir.views

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.contactosdir.components.MainIconButton
import com.example.contactosdir.components.MainTitle
import com.example.contactosdir.model.Contacto
import com.example.contactosdir.viewModels.ContactosViewModel
import com.example.contactosdir.viewModels.ContactoViewModel
import com.example.contactosdir.R
import java.io.File
import java.io.FileOutputStream


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
    val context = LocalContext.current

    var nombre by remember { mutableStateOf(contacto.nombre) }
    var apellidoPaterno by remember { mutableStateOf(contacto.apellidoPaterno) }
    var apellidoMaterno by remember { mutableStateOf(contacto.apellidoMaterno) }
    var correo by remember { mutableStateOf(contacto.correo) }
    var telefono by remember { mutableStateOf(contacto.telefono) }
    var domicilio by remember { mutableStateOf(contacto.domicilio) }
    var imageUri by remember { mutableStateOf(contacto.fotoUri?.let { Uri.parse(it) }) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Imagen de perfil
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Image(
                painter = if (imageUri != null)
                    rememberAsyncImagePainter(imageUri)
                else
                    painterResource(id = R.drawable.usuario), // imagen por defecto
                contentDescription = "Foto de perfil",
                modifier = Modifier.run {
                    size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                }
            )
            IconButton(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Editar imagen",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Campos de texto
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
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
                    val rutaLocal = when {
                        imageUri == null -> ""
                        imageUri!!.scheme == "content" -> guardarImagenEnInterno(context,
                            imageUri!!
                        )
                        else -> imageUri.toString() // ya era una ruta local, no se vuelve a guardar
                    }

                    val contactoEditado = contacto.copy(
                        nombre = nombre.trim(),
                        apellidoPaterno = apellidoPaterno.trim(),
                        apellidoMaterno = apellidoMaterno.trim(),
                        correo = correo.trim(),
                        telefono = telefono.trim(),
                        domicilio = domicilio.trim(),
                        fotoUri = rutaLocal
                    )
                    contactoVM.updateContacto(contactoEditado)
                    navController.popBackStack()
                } else {
                    // puedes mostrar un mensaje de error si lo deseas
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar contacto")
        }
    }
}

fun guardarImagenEnInterno(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return ""
    val fileName = "contacto_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)

    inputStream.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
    return file.absolutePath
}

