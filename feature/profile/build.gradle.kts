import Lib.AndroidX.coreKtx

plugins {
    `feature-module`
}

dependencies {
    implementation (corePresentation)
    implementation (dataRepository)
    implementation (featureAuthApi)
    implementation (featureProfileApi)
    implementation (coreKtx)
}