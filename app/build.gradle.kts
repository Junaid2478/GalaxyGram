plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.galaxygram"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.galaxygram"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        val nasaKey = (project.findProperty("NASA_API_KEY") as String?) ?: "DEMO_KEY"
        buildConfigField("String", "NASA_KEY", "\"$nasaKey\"")
    }

    buildTypes {
        debug { isMinifyEnabled = false }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}

dependencies {
    // Compose BOM (no per-artifact versions)
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation(libs.media3.exoplayer)
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.02.00"))

    implementation("androidx.compose.ui:ui")

    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.material:material-icons-extended")

    // Core / Lifecycle / Navigation
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // Images
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Videos
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")

    // Paging + Room (KSP)
    implementation("androidx.paging:paging-runtime-ktx:3.3.2")
    implementation("androidx.paging:paging-compose:3.3.2")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Hilt (KSP)
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")

    // Tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

}
