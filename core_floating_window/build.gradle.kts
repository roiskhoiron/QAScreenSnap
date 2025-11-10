plugins {
    id("com.screensnap.plugin.core")
}

android {
    namespace = "com.screensnap.core.floating_window"
}

dependencies {
    // Project
    implementation(project(":core_ui"))
}

