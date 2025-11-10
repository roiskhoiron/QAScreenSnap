package com.screensnap.plugin

import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(plugin = "com.android.application")
apply(plugin = "com.screensnap.plugin.android-base")
apply(plugin = "com.screensnap.plugin.extra.compose")

configure<ApplicationExtension> {

    buildFeatures {
        buildConfig = true
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
}

dependencies {
    // Dependencies are configured in the android-base and hilt plugins
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

// Apply KSP plugin
apply(plugin = "com.google.devtools.ksp")

// Optionally: Remove or migrate any kapt dependency usages in the module build files:
// - Replace 'kapt' dependencies with 'ksp' where available (especially for Hilt and Room)
// - Remove 'kapt' configuration from source sets and replace with 'ksp' if present
