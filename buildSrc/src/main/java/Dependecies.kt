object Versions {
    const val androidGradle = "3.2.0-beta05"
    const val androidx = "28.0.0-rc01"
    const val constraintlayout = "1.1.2"
    const val junit = "4.12"
    const val kotlin = "1.2.60"
    const val materialcomponents = "1.0.0-rc01"
    const val mockito = "2.21.0"
    const val mockitoKotlin = "2.0.0-RC1"
    const val rxjava2 = "2.2.0"
    const val rxandroid2 = "2.0.2"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradle}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinJdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val androidXAppCompat = "com.android.support:appcompat-v7:${Versions.androidx}"
    const val materialComponents = "com.google.android.material:material:${Versions.materialcomponents}"
    const val androidXRecyclerView = "com.android.support:recyclerview-v7:${Versions.androidx}"
    const val androidXConstraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintlayout}"

    const val junit = "junit:junit:${Versions.junit}"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"

    const val rxjava2 = "io.reactivex.rxjava2:rxjava:${Versions.rxjava2}"
    const val rxAndroid2 = "io.reactivex.rxjava2:rxandroid:${Versions.rxandroid2}"
}