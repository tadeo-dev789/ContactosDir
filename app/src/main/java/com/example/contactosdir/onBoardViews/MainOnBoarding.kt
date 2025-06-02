package com.example.contactosdir.onBoardViews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.contactosdir.R
import com.example.contactosdir.dataStore.StoreBoarding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainOnBoarding(navController: NavController, store: StoreBoarding) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Animación Lottie
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.contacts_animation))
            val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
            )

            Text(text = "Administra tus contactos")
            Text(text = "Guarda, organiza y edita todos tus contactos en un solo lugar.")

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        store.saveBoarding(true)
                    }
                    navController.navigate("home"){
                        popUpTo(0)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            ) {
                Text("Comenzar")
            }
        }
    }
}

