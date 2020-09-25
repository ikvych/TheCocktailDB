plugins {
    `feature-module`
}

dependencies {
    implementation (corePresentation)
    implementation (dataRepository)
    implementation (featureMainApi)
    implementation (featureAuthApi)
/*    implementation (platformFirebase)*/

    implementation (Lib.AndroidX.coreKtx)
}