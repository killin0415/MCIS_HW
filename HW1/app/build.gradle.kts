plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

android {
    namespace = "com.example.hw1"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.hw1"
        minSdk = 35
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation(platform("androidx.compose:compose-bom:2026.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation3:navigation3-runtime:1.1.0-beta01")
    implementation("androidx.navigation3:navigation3-ui:1.1.0-beta01")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
    implementation("androidx.compose.material:material-icons-core-android:1.7.8")
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3-android:2.11.0-alpha02")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2026.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.10.5")

    implementation(platform("io.insert-koin:koin-bom:4.1.1"))
    implementation("io.insert-koin:koin-android")
    implementation("io.insert-koin:koin-core-coroutines")
    implementation("io.insert-koin:koin-androidx-workmanager")
    implementation("io.insert-koin:koin-androidx-navigation")
    implementation("io.insert-koin:koin-androidx-compose")
    implementation("io.insert-koin:koin-compose")
    implementation("io.insert-koin:koin-compose-viewmodel")
    implementation("io.insert-koin:koin-ktor")
    implementation("io.insert-koin:koin-compose-navigation3-android:4.2.0-RC1")

}