plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin") // Seharusnya seperti ini, bukan 'alias(...)'
}

android {
    namespace = "com.example.workspave"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.workspave"
        minSdk = 24
        targetSdk = 36
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
        // Ganti ke Java 17, standar baru untuk Android
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    // Tambahkan ini untuk mengaktifkan ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Dependensi bawaan
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Dependensi yang kita tambahkan (sekarang pakai 'libs')
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.glide)

    // Dependensi untuk testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //foto bulat
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.google.code.gson:gson:2.10.1")
}