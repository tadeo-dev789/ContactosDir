package com.example.contactosdir.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color // Asegúrate de que esta importación esté presente

// Importa tus nuevos colores azules definidos en Colors.kt
// (Asumo que los colores Blue80, LightBlue80, etc., están en un archivo Colors.kt separado
// o en este mismo archivo, pero si están en Colors.kt, asegúrate de que se importen correctamente)

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,        // Un azul claro vibrante para elementos principales en tema oscuro
    secondary = LightBlue80, // Un azul cian claro para elementos secundarios en tema oscuro
    tertiary = SkyBlue80     // Un azul cielo para elementos terciarios en tema oscuro
)

private val LightColorScheme = lightColorScheme(
    primary = OceanBlue40,   // Un azul océano profundo para elementos principales en tema claro
    secondary = MidBlue40,   // Un azul acero para elementos secundarios en tema claro
    tertiary = DarkBlue40    // Un azul medianoche para elementos terciarios en tema claro

)

@Composable
fun ContactosDirTheme(
    darkTheme: Boolean = false,
    // El color dinámico está disponible en Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // Si el tema es oscuro, usa el esquema de color dinámico oscuro; de lo contrario, usa el claro
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        // Si no hay color dinámico o la versión de Android es anterior a la 12
        darkTheme -> DarkColorScheme // Usa el esquema de color oscuro definido estáticamente
        else -> LightColorScheme    // Usa el esquema de color claro definido estáticamente
    }

    // Aplica el esquema de color y la tipografía a tu MaterialTheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asumo que 'Typography' está definido en otro lugar
        content = content
    )
}