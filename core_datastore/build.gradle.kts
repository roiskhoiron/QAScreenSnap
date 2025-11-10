plugins {
    id("com.screensnap.plugin.core")
}

android {
    namespace = "com.screensnap.core.datastore"
}

dependencies {

    implementation("androidx.datastore:datastore-preferences:1.0.0")
}
