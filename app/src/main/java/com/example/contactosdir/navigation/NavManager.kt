package com.example.contactosdir.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
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
import com.example.contactosdir.dataStore.StoreBoarding
import com.example.contactosdir.onBoardViews.MainOnBoarding
import com.example.contactosdir.views.DetailView
import com.example.contactosdir.views.SplashScreen

@Composable
fun NavManager(contactoVM: ContactosViewModel, contactosVM: ContactoViewModel){
    val navController = rememberNavController()

    val context= LocalContext.current
    val dataStore= StoreBoarding(context)
    val store=dataStore.getStoreBoarding.collectAsState(initial = true)


    NavHost(navController = navController, startDestination = if(store.value ==true) "Home" else "Splash"){
        composable("onBoarding"){
            MainOnBoarding(navController,dataStore)
        }
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

        composable("DetailView/{id}", arguments = listOf(navArgument("id") {
            type = NavType.IntType
        })) {
            val id = it.arguments?.getInt("id") ?: 0
            DetailView(navController, contactoVM, contactosVM, id)
        }

        composable("Splash"){
            SplashScreen(navController, store.value)
        }


    }
}
