plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.parkirku"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.parkirku"
        minSdk = 24
        targetSdk = 34
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
        viewBinding = true
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

    //auth
    // Import the BoM for the Firebase platform
    //implementation(platform(libs.firebase.bom))
    // Declare the dependency for the Firebase Authentication library
//    implementation(libs.google.firebase.auth.ktx)

    //foto lokal
    implementation (libs.coil.compose)



    //kedua untuk auth
    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom.v3200))


    //gms
    implementation (libs.play.services.auth.v1920)

    // Firebase Authentication
    implementation(libs.com.google.firebase.firebase.auth.ktx)

    // Optional: Google Sign-In
    implementation(libs.play.services.auth)


    //untuk refresh
    implementation (libs.accompanist.swiperefresh.v0249beta)


    implementation (libs.androidx.material.icons.extended)

    //UNTUK MAINSCREEN
    implementation(libs.material3)
    implementation(libs.androidx.runtime.v105)
    implementation(libs.androidx.ui.v105)
    implementation(libs.androidx.ui.tooling.v105)
    implementation(libs.androidx.ui.tooling.preview.v105)
    implementation(libs.androidx.navigation.compose.v280)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}