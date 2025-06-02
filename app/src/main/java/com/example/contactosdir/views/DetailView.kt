package com.example.contactosdir.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contactosdir.components.MainIconButton
import com.example.contactosdir.components.MainTitle
import com.example.contactosdir.model.Contacto
import com.example.contactosdir.viewModels.ContactosViewModel
import com.example.contactosdir.viewModels.ContactoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailView(
    navController: NavController,
    contactoVM: ContactosViewModel,
    contactosVM: ContactoViewModel,
    id: Int
) {
    val contacto by contactosVM.selectedContacto.collectAsState()

    LaunchedEffect(id) {
        contactosVM.selectContactoById(id)
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "Detalle del Contacto") },
                navigationIcon = {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.popBackStack()
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("EditView/${contacto?.id ?: 0}")
                },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar Contacto",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (contacto == null) {
                    Text("Cargando...", style = MaterialTheme.typography.bodyLarge)
                } else {
                    contacto?.let { c ->
                        DetailContent(c, context, navController)
                    }
                }
            }
        }
    )
}

@Composable
fun DetailContent(
    contacto: Contacto,
    context: Context,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "${contacto.nombre} ${contacto.apellidoPaterno} ${contacto.apellidoMaterno}",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),      // <-- Aquí
                textAlign = TextAlign.Center             // <-- Y aquí
            )

            Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ActionButton("Llamar", Icons.Default.Call) {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${contacto.telefono}")
                    }
                    context.startActivity(intent)
                }
                ActionButton("Mensaje", Icons.Default.Send) {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:${contacto.telefono}")
                    }
                    context.startActivity(intent)
                }
                ActionButton("Correo", Icons.Default.Email) {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${contacto.correo}")
                    }
                    context.startActivity(intent)
                }
                ActionButton("Ubicación", Icons.Default.LocationOn) {
                    val domicilio = contacto.domicilio ?: ""
                    if (domicilio.isNotBlank()) {
                        val domicilioEncoded = Uri.encode(domicilio)
                        // Usamos el link web con api=1 para buscar dirección
                        val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$domicilioEncoded")
                        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        } else {
                            // fallback si no tiene Google Maps
                            val fallbackIntent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(fallbackIntent)
                        }
                    }
                }

            }

            Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoSection("Teléfono", Icons.Default.Phone, contacto.telefono)
                InfoSection("Correo electrónico", Icons.Default.Email, contacto.correo)
                InfoSection("Domicilio", Icons.Default.Home, contacto.domicilio ?: "No especificado")
            }
        }
    }
}

@Composable
fun ActionButton(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun InfoSection(title: String, icon: ImageVector, info: String) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                text = info,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
