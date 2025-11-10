plugins {
    id("com.screensnap.plugin.core")
}

android {
    namespace = "com.screensnap.core.notification"
}

dependencies {
    // Project
    implementation(project(":core_ui"))

    implementation("androidx.core:core:1.12.0")
}
