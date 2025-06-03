package com.example.contactosdir.components

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactosdir.model.Contacto // Asegúrate de que la ruta sea correcta
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Call
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest


@Composable
fun ContactoCard(
    contacto: Contacto,
    onEditClick: (Contacto) -> Unit,
    onDeleteClick: (Contacto) -> Unit,
    onShowClick: (Contacto) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current // para lanzar el intent
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen o ícono de contacto
            if (!contacto.fotoUri.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(Uri.parse(contacto.fotoUri))
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "Foto de Contacto",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Icono de Contacto",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del Contacto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${contacto.nombre} ${contacto.apellidoPaterno} ${contacto.apellidoMaterno}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable{
                        onShowClick(contacto)
                    }

                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Teléfono",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = contacto.telefono,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Correo Electrónico",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,

                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = contacto.correo,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Botones de Acción
            Column (
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                IconButton(onClick = { onEditClick(contacto) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Contacto",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onDeleteClick(contacto) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar Contacto",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactoCardPreview() {
    // Creas un Contacto de ejemplo para la preview
    val sampleContacto = Contacto(
        id = 1,
        nombre = "Juan",
        apellidoPaterno = "Pérez",
        apellidoMaterno = "García",
        correo = "juan.perez@example.com",
        telefono = "555-123-4567"
    )
    MaterialTheme { // Asegúrate de envolverlo en tu tema si usas colores del tema
        ContactoCard(
            contacto = sampleContacto,
            onEditClick = { /* Acción para editar en preview */ },
            onDeleteClick = { /* Acción para eliminar en preview */ },
            onShowClick={ /* Acción para eliminar en preview */ }
        )
    }
}

@Composable
fun MainTitle(title: String) {
    Text(text = title, color = Color.White, fontWeight = FontWeight.Bold)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp)
    )
}