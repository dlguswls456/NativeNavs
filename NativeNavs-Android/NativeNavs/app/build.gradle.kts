plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.circus.nativenavs"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.circus.nativenavs"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.text.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    //krossbow
    implementation(libs.krossbow.stomp.core)
    implementation(libs.krossbow.websocket.okhttp)
    implementation(libs.krossbow.stomp.moshi)

    implementation(libs.material.calendarview){
        exclude(group = "com.android.support", module = "support-compat")
    }
    implementation(libs.threetenabp)

    //이미지 Glide
    implementation(libs.glide)

    //QR
    implementation(libs.core)
    implementation(libs.zxing.android.embedded)

    // FCM
    implementation(libs.firebase.bom)
    implementation(libs.google.firebase.messaging.ktx)
}