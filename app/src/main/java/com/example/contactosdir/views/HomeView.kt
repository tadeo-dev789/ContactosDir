package com.example.contactosdir.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contactosdir.components.ContactoCard
import com.example.contactosdir.components.FloatButton
import com.example.contactosdir.components.MainTitle
import com.example.contactosdir.viewModels.ContactoViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import com.airbnb.lottie.compose.*
import com.example.contactosdir.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    contactosVM: ContactoViewModel
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "Directorio") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatButton {
                navController.navigate("AddView")
            }
        }
    ) { paddingValues ->
        ContentHomeView(paddingValues, navController, contactosVM)
    }
}
@Composable
fun ContentHomeView(
    paddingValues: PaddingValues,
    navController: NavController,
    contactosVM: ContactoViewModel
) {
    var searchText by remember { mutableStateOf("") }
    val allContactos by contactosVM.contactosList.collectAsState()
    val contactosList = allContactos.filter {
        it.nombre.contains(searchText, ignoreCase = true) ||
                it.apellidoPaterno.contains(searchText, ignoreCase = true) ||
                it.apellidoMaterno.contains(searchText, ignoreCase = true) ||
                it.telefono.contains(searchText)
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar contacto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        when {
            allContactos.isEmpty() && searchText.isBlank() -> {
                // Mostrar animación solo si no hay contactos y no se está buscando
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animtel))
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .height(200.dp)
                            .padding(16.dp)
                            .scale(3f)
                    )

                    Text(
                        text = "Aún no tienes contactos guardados.\n¡Toca el botón + para agregar uno!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

            contactosList.isNotEmpty() -> {
                LazyColumn {
                    items(
                        items = contactosList,
                        key = { it.id }
                    ) { contacto ->
                        val deleteAction = SwipeAction(
                            icon = rememberVectorPainter(Icons.Default.Delete),
                            background = Color.Red,
                            onSwipe = { contactosVM.deleteContacto(contacto) }
                        )

                        val editAction = SwipeAction(
                            icon = rememberVectorPainter(Icons.Default.Edit),
                            background = Color.Blue,
                            onSwipe = { navController.navigate("EditView/${contacto.id}") }
                        )

                        SwipeableActionsBox(
                            startActions = listOf(editAction),
                            endActions = listOf(deleteAction),
                            swipeThreshold = 150.dp
                        ) {
                            ContactoCard(
                                contacto = contacto,
                                onEditClick = {
                                    navController.navigate("EditView/${contacto.id}")
                                },
                                onDeleteClick = {
                                    contactosVM.deleteContacto(contacto)
                                },
                                onShowClick = {
                                    navController.navigate("DetailView/${contacto.id}")
                                }
                            )
                        }
                    }
                }
            }

            // Si hay búsqueda y no hay resultados, no mostrar nada (pantalla vacía)
        }
    }
}

