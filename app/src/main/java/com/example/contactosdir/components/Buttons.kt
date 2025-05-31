package com.example.contactosdir.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FloatButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Agregar"
        )
    }
}

@Composable
fun MainIconButton(icon: ImageVector, onClick:() -> Unit){
    IconButton(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = null,
            tint = Color.White)
    }
}

@Composable
fun CircleButton(
    icon: Painter,
    enabled: Boolean = false,
    onClick: () -> Unit
){
    Button(onClick = onClick, shape = CircleShape,
        contentPadding = PaddingValues(8.dp),
        enabled=enabled,
        modifier=Modifier.padding(horizontal=15.dp)
    ){
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}