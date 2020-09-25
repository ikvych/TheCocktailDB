plugins {
    `feature-module`
}

dependencies {
    implementation (corePresentation)
    implementation (dataRepository)
    implementation (featureProfileApi)
/*    implementation (platformFirebase)*/

    implementation (Lib.AndroidX.coreKtx)
}