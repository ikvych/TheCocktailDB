import Lib.AndroidX.Lifecycle.liveData
import Lib.AndroidX.Lifecycle.liveDataKtx

plugins {
    `android-lib-module`
}

dependencies {
    implementation(coreCommon)
    implementation(coreKodein)

    implementation(dataRepository)

    implementation(dataLocal)
    implementation(dataDatabase)
    implementation(dataNetwork)

    implementation(liveData)
    implementation(liveDataKtx)

}
