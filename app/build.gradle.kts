plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.app.ebfitapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.ebfitapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String","RapidAPI_KEY","\"X-RapidAPI-Key: c7de494d29msh11c396e17354e22p1d1ee5jsn71bfc00fa0e2\"")
        buildConfigField("String","RapidAPI_HOST","\"X-RapidAPI-Host: exercisedb.p.rapidapi.com\"")
        buildConfigField("String","RapidAPI_BASE","\"https://exercisedb.p.rapidapi.com/\"")
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
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    val nav_version = "2.7.5"
    val rxjava_version = "2.1.1"
    val retrofit_version = "2.9.0"
    val coroutine_version = "1.7.1"

    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")

    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation("com.airbnb.android:lottie:6.1.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("me.tankery.lib:circularSeekBar:1.4.2")

    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofit_version")

    implementation("io.reactivex.rxjava2:rxjava:$rxjava_version")
    implementation("io.reactivex.rxjava2:rxandroid:$rxjava_version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutine_version")

    implementation("com.google.firebase:firebase-messaging:23.4.0")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}