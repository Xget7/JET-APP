// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false
    kotlin("jvm") version "1.8.0" apply false
}
buildscript{
    dependencies{
        classpath (libs.hilt.android.gradle.plugin)
    }
    repositories {
        google()
    }
}
repositories {
    google()
}
