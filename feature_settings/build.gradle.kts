plugins {
    id("com.screensnap.plugin.feature")
}

android {
    namespace = "com.screensnap.feature.settings"
}

dependencies {
    // Project
    implementation(project(":core_datastore"))
}

