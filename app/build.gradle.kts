plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.kotlin)
}

android {
    compileSdk = 36
    namespace = "it.bitprepared.prbm.mobile"

    defaultConfig {
        applicationId = "it.bitprepared.prbm.mobile"
        minSdk = 19
        targetSdk = 36
        versionCode = 26
        versionName = "v1.1.4"
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(libs.androidx.multidex)
    coreLibraryDesugaring(libs.desugarjdklibs)
}
