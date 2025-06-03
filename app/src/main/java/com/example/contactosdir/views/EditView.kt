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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest // Importar ImageRequest
import com.example.contactosdir.R
import com.example.contactosdir.components.MainIconButton
import com.example.contactosdir.components.MainTitle
import com.example.contactosdir.model.Contacto
import com.example.contactosdir.viewModels.ContactosViewModel
import com.example.contactosdir.viewModels.ContactoViewModel
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
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando...")
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

    // Usamos remember para mantener el estado de la URI de la imagen seleccionada.
    // Inicialmente, será la URI guardada en el contacto.
    var imageUri by remember { mutableStateOf(contacto.fotoUri?.let { Uri.parse(it) }) }

    // Un LaunchedEffect para actualizar imageUri si el contacto cambia (ej. al recargar la vista)
    LaunchedEffect(contacto.fotoUri) {
        imageUri = contacto.fotoUri?.let { Uri.parse(it) }
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri // Actualiza el estado con la nueva URI seleccionada
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pasamos imageUri al composable ProfileImage
        ProfileImage(imageUri = imageUri, onClick = { launcher.launch("image/*") })

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
                    val rutaLocal = imageUri?.let {
                        if (it.scheme == "content") {
                            guardarImagenEnInterno(context, it)
                        } else {
                            it.toString()
                        }
                    } ?: ""

                    val contactoEditado = contacto.copy(
                        nombre = nombre.trim(),
                        apellidoPaterno = apellidoPaterno.trim(),
                        apellidoMaterno = apellidoMaterno.trim(),
                        correo = correo.trim().toLowerCase(),
                        telefono = telefono.trim(),
                        domicilio = domicilio.trim(),
                        fotoUri = rutaLocal
                    )
                    contactoVM.updateContacto(contactoEditado)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar contacto")
        }
    }
}

@Composable
fun ProfileImage(imageUri: Uri?, onClick: () -> Unit) {
    val context = LocalContext.current // Obtenemos el contexto para ImageRequest.Builder

    Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.BottomEnd) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(imageUri) // Aquí es donde pasamos el URI
                .crossfade(true) // Opcional: efecto de fade al cargar
                .error(R.drawable.usuario) // Si Coil no puede cargar imageUri, muestra esta imagen
                .placeholder(R.drawable.usuario) // Mientras carga, muestra esta imagen
                .build()
        )

        Image(
            painter = painter,
            contentDescription = "Foto de perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
        )

        IconButton(
            onClick = onClick,
            modifier = Modifier
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