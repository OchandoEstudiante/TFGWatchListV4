import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
}

val localProperties =Properties().apply{
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.example.tfgwatchlist"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.tfgwatchlist"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "API_TOKEN",
            "\"${localProperties["API_TOKEN"]}\""
        )
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

    buildFeatures{
        viewBinding= true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Lifecycle viewmodel en compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    //View model
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    //Live Data
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    //Activity
    implementation("androidx.activity:activity-ktx:1.2.2")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //Los converters de Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    //
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.6")
    //
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("androidx.fragment:fragment-ktx:1.5.4")
    // Navigation Component (para usar navigation bar y navegar entre fragments)
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation(libs.glide)
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}