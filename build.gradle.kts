// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
        //noinspection UseTomlInstead
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.0")

    }
    repositories {
        jcenter()
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id ("org.jetbrains.kotlinx.kover") version "0.6.1" apply false
    id ("de.mannodermaus.android-junit5") version "1.8.2.1" apply false
}




repositories {
    google()
}
