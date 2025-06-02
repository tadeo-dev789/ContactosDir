plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    kotlin ("kapt") //Correccion de errores
    id ("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.contactosdir"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.contactosdir"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Room
    val room_version = "2.6.1"
    implementation ("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

// Dagger Core
    implementation ("com.google.dagger:dagger:2.46.1")
    kapt ("com.google.dagger:dagger-compiler:2.46.1")

// Dagger Android
    api ("com.google.dagger:dagger-android:2.46.1")
    api ("com.google.dagger:dagger-android-support:2.46.1")
    kapt ("com.google.dagger:dagger-android-processor:2.46.1")


    //  Dagger Hilt
    implementation ("com.google.dagger:hilt-android:2.46.1")
    kapt ("com.google.dagger:hilt-compiler:2.46.1")

    //Navigation
    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Swipe
    implementation ("me.saket.swipe:swipe:1.1.1")
    //--OnBoarding
    implementation ("com.google.accompanist:accompanist-pager:0.15.0")

    //--Navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")
    //--DataStore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    //-- Lottie
    implementation ("com.airbnb.android:lottie-compose:5.2.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}