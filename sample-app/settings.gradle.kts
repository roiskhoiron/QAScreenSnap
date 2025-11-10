pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        kotlin("android") version "1.9.10"
        kotlin("jvm") version "1.9.10"
        kotlin("kapt") version "1.9.10"
        id("com.google.dagger.hilt.android") version "2.48.1"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(org.gradle.api.initialization.dsl.RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "sample-app"

// include local SDK module if present
include(":screensnap-sdk")

