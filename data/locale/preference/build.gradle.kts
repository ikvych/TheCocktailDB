import Lib.AndroidX.Lifecycle.liveDataKtx

plugins {
    `android-lib-module`
}

dependencies {
    implementation(coreCommon)
    implementation(coreKodein)
    implementation(liveDataKtx)
    implementation(dataLocal)
}
