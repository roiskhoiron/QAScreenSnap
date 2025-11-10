package com.screensnap.plugin.extra

import com.screensnap.utils.implementation
import com.screensnap.utils.ksp
import org.gradle.kotlin.dsl.dependencies

apply(plugin = "com.google.dagger.hilt.android")

dependencies {
    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
}