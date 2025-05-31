package com.example.contactosdir.views // Asegúrate que el package sea el correcto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Correcta importación para items en LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit // Icono para la acción de editar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.contactosdir.components.ContactoCard // Tu componente para mostrar un contacto
import com.example.contactosdir.components.FloatButton
import com.example.contactosdir.components.MainTitle
import com.example.contactosdir.model.Contacto // El modelo de datos de Contacto
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import com.example.contactosdir.viewModels.ContactoViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    // Inyecta el ViewModel usando Hilt si lo configuraste
    contactosVM: ContactoViewModel
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "Directorio") }, // Título de la App
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatButton {
                navController.navigate("AddView") // Navega a la vista para añadir contactos
            }
        }
    ) { paddingValues -> // Renombrado 'it' a 'paddingValues' para mayor claridad
        ContentHomeView(paddingValues, navController, contactosVM)
    }
}

@Composable
fun ContentHomeView(
    paddingValues: PaddingValues,
    navController: NavController,
    contactosVM: ContactoViewModel
) {
    Column(
        modifier = Modifier.padding(paddingValues) // Aplica el padding del Scaffold
    ) {
        // Observa la lista de contactos desde el ViewModel
        val contactosList by contactosVM.contactosList.collectAsState()

        if (contactosList.isEmpty()) {
            // Puedes mostrar un mensaje si la lista está vacía
            // Text("No hay contactos aún. ¡Añade uno!", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(
                    items = contactosList,
                    key = { contacto -> contacto.id } // Proporciona una clave única para cada item
                ) { contacto ->
                    // Acción de eliminar al deslizar hacia la izquierda (end)
                    val deleteAction = SwipeAction(
                        icon = rememberVectorPainter(Icons.Default.Delete),
                        background = Color.Red,
                        onSwipe = { contactosVM.deleteContacto(contacto) }
                    )

                    // Acción de editar al deslizar hacia la derecha (start)
                    // (O puedes usar otra acción si prefieres)
                    val editAction = SwipeAction(
                        icon = rememberVectorPainter(Icons.Default.Edit),
                        background = Color.Blue, // Puedes elegir otro color
                        onSwipe = { navController.navigate("EditView/${contacto.id}") }
                    )

                    SwipeableActionsBox(
                        startActions = listOf(editAction),  // Acción al deslizar desde el inicio (ej. Editar)
                        endActions = listOf(deleteAction),   // Acción al deslizar desde el final (ej. Eliminar)
                        swipeThreshold = 150.dp // Ajusta el umbral de deslizamiento según necesites
                    ) {
                        // Tu componente ContactoCard para mostrar la información del contacto
                        ContactoCard(
                            contacto = contacto,
                            onEditClick = {
                                navController.navigate("EditView/${contacto.id}")
                            },
                            onDeleteClick = {
                                contactosVM.deleteContacto(contacto)
                            }
                        )
                    }
                }
            }
        }
    }
}

// Preview para HomeView (opcional pero recomendado)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    // Puedes crear un NavController de prueba y un ViewModel de prueba si es necesario
    // o usar un NavController y ViewModel simplificados para la preview.
    val navController = rememberNavController()
    // Para la preview, puedes instanciar un ViewModel mock o uno real si no tiene dependencias complejas
    // o si Hilt está configurado para previews. Por simplicidad, aquí no lo inyectamos.
    // En un caso real, necesitarías configurar esto para que el ViewModel funcione en la preview.
    // val previewViewModel = ContactoViewModel(FakeContactosRepository())

    MaterialTheme { // Asumiendo que tienes un MaterialTheme configurado
        // HomeView(navController = navController, contactosVM = previewViewModel)
        // Para una preview más simple sin ViewModel:
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
                FloatButton { /* No action in preview */ }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                val sampleContacto = Contacto(1, "Ana", "Ruiz", "Lopez", "ana.ruiz@example.com", "555-001122")
                ContactoCard(
                    contacto = sampleContacto,
                    onEditClick = { },
                    onDeleteClick = { }
                )
            }
        }
    }
}