plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 34
    namespace = "it.bitprepared.prbm.mobile"

    defaultConfig {
        applicationId = "it.bitprepared.prbm.mobile"
        minSdk = 15
        targetSdk = 34
        versionCode = 11
        versionName = "v0.6.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}
dependencies {
    implementation(libs.androidx.annotations)

    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // RxJava
    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)
}
