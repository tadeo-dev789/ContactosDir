package com.example.contactosdir.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.contactosdir.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, store: Boolean?) {
    val destination = if (store == true) "Home" else "onBoarding"

    LaunchedEffect(Unit) {
        delay(1500) // tiempo visible del splash
        navController.navigate(destination) {
            popUpTo(0) // limpia el backstack para evitar volver al Splash
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.phone__1_),
            contentDescription = "Splash Logo"
        )
    }
}
