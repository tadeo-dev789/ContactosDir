package com.example.contactosdir.onBoardViews

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.contactosdir.dataStore.StoreBoarding
import com.example.contactosdir.model.PageData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.example.contactosdir.R

@OptIn(ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class)
@Composable
fun MainOnBoarding(navController: NavController,store: StoreBoarding){
    val items= ArrayList<PageData>()

    items.add(
        PageData(
            R.raw.contacts_animation,
            "Bienvenido",
            " a tu nueva experiencia"
        )
    )
    items.add(
        PageData(
            R.raw.anim2,
            "Explora",
            "Conoce nuestras funciones"
        )
    )
    items.add(
        PageData(
            R.raw.anim3,
            "Comienza",
            "Prepárate para usar la app"
        )
    )

    val pagerState= rememberPagerState(
        pageCount = items.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0
    )

    OnBoardingPager(
        item=items, pagerState=pagerState,modifier= Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White), navController,store
    )
}