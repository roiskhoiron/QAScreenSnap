plugins {
    id("com.screensnap.plugin.feature")
}

android {
    namespace = "com.screensnap.feature.home"
}

dependencies {
    // SDK
    implementation(project(":screensnap-sdk"))
    // Project
    implementation(project(":core_datastore"))
    implementation(project(":core_screen_recorder"))
    implementation(project(":core_notification"))
    implementation(project(":core_camera"))
}

