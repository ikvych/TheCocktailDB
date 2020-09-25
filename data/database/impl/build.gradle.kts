import Lib.AndroidX.Lifecycle.liveData
import Lib.AndroidX.Lifecycle.liveDataKtx

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

android {
    defaultConfig.javaCompileOptions.annotationProcessorOptions.arguments = mapOf("room.incremental" to "true")
}

dependencies {
    implementation(coreCommon)
    implementation(coreKodein)

    implementation(dataDatabase)

    implementation(Lib.Kotlin.stdlib)

    implementation(liveDataKtx)
    implementation(liveData)

    implementation(Lib.AndroidX.Room.common)
    implementation(Lib.AndroidX.Room.runtime)
    implementation(Lib.AndroidX.Room.ktx)

    kapt(Lib.AndroidX.Room.compiler)
}
