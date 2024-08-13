plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.kotlin)
}

android {
    compileSdk = 34
    namespace = "it.bitprepared.prbm.mobile"

    defaultConfig {
        applicationId = "it.bitprepared.prbm.mobile"
        minSdk = 19
        targetSdk = 34
        versionCode = 17
        versionName = "v1.0.2"
        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.annotations)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.mdc)

    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // RxJava
    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)

    // Firebase
    implementation(platform(libs.firebase.bom))

    implementation(libs.androidx.multidex)
    coreLibraryDesugaring(libs.desugarjdklibs)
}
