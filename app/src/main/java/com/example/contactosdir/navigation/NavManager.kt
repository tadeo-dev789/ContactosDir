package com.example.contactosdir.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.contactosdir.viewModels.ContactosViewModel
import com.example.contactosdir.viewModels.ContactoViewModel
import com.example.contactosdir.views.AddView
import com.example.contactosdir.views.EditView
import com.example.contactosdir.views.HomeView


@Composable
fun NavManager(contactoVM: ContactosViewModel, contactosVM: ContactoViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Home"){
        composable("Home"){
            HomeView(navController,contactosVM)
        }
        composable("AddView"){
            AddView(navController,contactoVM,contactosVM)
        }

        composable("EditView/{id}", arguments = listOf(navArgument("id") {
            type = NavType.IntType
        })) {
            val id = it.arguments?.getInt("id") ?: 0
            EditView(navController, contactoVM, contactosVM, id)
        }


    }
}
