plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "ca.hccis.buspass"
    compileSdk = 35

    defaultConfig {
        applicationId = "ca.hccis.buspass"
        minSdk = 29
        targetSdk = 34
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
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

    // ChatBot
    implementation(libs.generativeai)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.runtime.livedata)

    // https://mvnrepository.com/artifact/androidx.compose.material3/material3-window-size-class
    implementation("androidx.compose.material3:material3-window-size-class:1.4.0-alpha10")

    testImplementation(libs.junit)
    implementation("com.google.code.gson:gson:2.7")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.core:core-splashscreen:1.0.0")
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version") // For annotation processing
    implementation("androidx.room:room-ktx:$room_version") // Kotlin extensions for Room
    implementation("androidx.compose.material:material-icons-extended:1.4.3")

    //gson
    implementation("com.google.code.gson:gson:2.10.1")
    //qr code
    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    implementation("com.google.zxing:core:3.5.2")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // For AppWidgets support
    implementation ("androidx.glance:glance-appwidget:1.1.0")
    // For interop APIs with Material 3
    implementation ("androidx.glance:glance-material3:1.1.0")
    // For interop APIs with Material 2
    implementation ("androidx.glance:glance-material:1.1.0")


}