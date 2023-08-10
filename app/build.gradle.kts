import java.util.Properties

plugins {
    // Add the Kotlin serialization plugin
    kotlin("plugin.serialization") version "1.8.0"
    // Alias for common Android plugins
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    // Add the Kotlinx serialization plugin
    id("kotlinx-serialization")
    // Add the Dagger Hilt plugin for Android
    id("com.google.dagger.hilt.android")
    // Add the KSP (Kotlin Symbol Processing) plugin to generate code at compile-time
    id("com.google.devtools.ksp") version "1.8.0-1.0.9"
    // Add the Android JUnit 5 plugin to run unit tests on Android
    id("de.mannodermaus.android-junit5")
    // Add the Kover plugin to generate code coverage reports
    id("kover")
}

junitPlatform {
    configurationParameter("junit.jupiter.testinstance.lifecycle.default", "per_class")
}



android {
    buildFeatures.buildConfig = true

    namespace = "xget.dev.jet"
    compileSdk = 33
    defaultConfig {

        applicationId = "xget.dev.jet"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "xget.dev.jet.util.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField(
            "String",
            "COGNITO_POOL_ID",
            "\"${properties.getProperty("COGNITO_POOL_ID")}\""
        )
        buildConfigField(
            "String",
            "AWS_MQTT_ENDPOINT",
            "\"${properties.getProperty("AWS_MQTT_ENDPOINT")}\""
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
        correctErrorTypes = true
    }
    externalNativeBuild {
        ndkBuild {
            ndkPath = "src/main/jni/Android.mk"
        }
    }

    //log Test
    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests.isReturnDefaultValues = true

    }

}

dependencies {
    // Core
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.appcompat)
    api(libs.androidx.activity.ktx)
    api(libs.androidx.fragment.ktx)

    // Compose UI Controller
    implementation(libs.accompanist.systemuicontroller)

    // Glide images
    //noinspection UseTomlInstead
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")

    // Paging Compose
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // Ktor
    implementation(libs.logback.classic)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.client.logging)

    // MQTT V5
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.paho.mqtt.android)

    // AWS MQTT
    implementation(libs.aws.android.sdk.iot)
    implementation(libs.aws.android.sdk.core)
    implementation(libs.aws.android.sdk.cognitoidentityprovider)

    // Dagger Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.dagger.android.support)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)
    kapt(libs.dagger.android.processor)

    // JWT AUTH
    api(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.orgjson)

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.symbol.processing.api)
    ksp(libs.androidx.room.compiler)

    // Lottie
    implementation(libs.lottie.compose)

    // Location
    implementation(libs.play.services.location)


    //Instrumentation Testing
    androidTestImplementation (libs.androidx.navigation.testing)

    // For instrumented tests HILT .
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation (libs.hilt.android.testing)
    // ...with Kotlin.
    kaptAndroidTest (libs.hilt.android.compiler)

    //Expresso and Junit
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(platform(libs.compose.bom))
    //Ui test
    androidTestImplementation(libs.ui.test.junit4)
    //Ktor Mock
    androidTestImplementation(libs.ktor.client.mock)
    androidTestImplementation(libs.mockk)
    //Unit test
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.mockk)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.vintage.engine)
    testImplementation(libs.kotlinx.coroutines.test)

    // Debugging
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    debugImplementation (libs.androidx.monitor)

}
