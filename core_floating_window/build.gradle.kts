plugins {
    id("com.screensnap.plugin.core")
    id("com.screensnap.plugin.extra.compose")
}

android {
    namespace = "com.screensnap.core.floating_window"
}

dependencies {
    // Project
    implementation(project(":core_ui"))
    // Foundation for gestures
    implementation("androidx.compose.foundation:foundation:1.6.2")
}

