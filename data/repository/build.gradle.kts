import Lib.AndroidX.Lifecycle.liveDataKtx

plugins {
    `android-lib-module`
}

dependencies {
    implementation(coreCommon)

    implementation(liveDataKtx)

    api(Lib.Kotlin.coroutines)
}