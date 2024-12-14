plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.walkwell"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.walkwell"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

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

    // Core Hilt dependency
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Hilt viewmodel
//    implementation(libs.androidx.hilt.lifecycle.viewmodel)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0-alpha03")
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.4")

    // For permissions in android
    implementation(libs.accompanist.permissions)

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    // Kotlin Coroutines dependencies
    val coroutinesVersion = "1.7.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // Hilt ViewModel compiler
    ksp(libs.androidx.hilt.compiler)

    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.material:material:1.6.8")
    implementation("androidx.compose.ui:ui-tooling:1.1.0-beta01")

    // Google Maps Location Services
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:17.0.0")

    implementation ("com.google.accompanist:accompanist-permissions:0.31.3-beta")
    implementation("com.google.maps.android:maps-compose:2.9.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-service:2.4.0")

    //Coil
    implementation("io.coil-kt:coil-svg:2.4.0")

    // compose live data
    implementation("androidx.compose.runtime:runtime-livedata:1.7.6")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
}