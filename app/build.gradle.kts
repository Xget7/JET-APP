
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    id("com.google.dagger.hilt.android")
    kotlin("jvm") version "1.8.0" apply false
    id("com.google.devtools.ksp") version "1.8.0-1.0.9"

}


android {
    namespace = "xget.dev.jet"
    compileSdk = 33

    defaultConfig {
        applicationId = "xget.dev.jet"
        minSdk = 26
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
      //  jvmTarget = "1.17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes= true
    }
    externalNativeBuild {
        ndkBuild {
            ndkPath = "src/main/jni/Android.mk"
        }
    }
}

dependencies {

    //Core
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    // Paging Compose
    implementation (libs.accompanist.pager)
    implementation (libs.accompanist.pager.indicators)
    //Ktor
    implementation (libs.ktor.client.android)
    implementation (libs.ktor.client.cio)
    implementation (libs.ktor.client.logging)
    implementation (libs.logback.classic)
    implementation (libs.kotlinx.serialization.json)
    implementation (libs.ktor.client.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.content.negotiation)


    //MQTT V5
    implementation (libs.androidx.legacy.support.v4)
    implementation (libs.paho.mqtt.android)
    //Dagger Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt (libs.androidx.hilt.compiler)
    kapt (libs.dagger.android.processor)
    implementation (libs.androidx.hilt.navigation.compose)
    implementation (libs.dagger.android.support)

    //JWT AUTH
    api(libs.jjwt.api)
    runtimeOnly (libs.jjwt.impl)
    runtimeOnly(libs.jjwt.orgjson)
    //Room

    implementation (libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    // To use Kotlin Symbol Processing (KSP)
    implementation(libs.symbol.processing.api)
    ksp(libs.androidx.room.compiler)



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)



}