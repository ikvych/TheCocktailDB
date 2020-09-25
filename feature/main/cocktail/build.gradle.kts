plugins {
    `feature-module`
}

dependencies {
    implementation (corePresentation)
    implementation (dataRepository)
    implementation (featureMainCocktailFilter)
    implementation (featureSearchApi)
/*    implementation (platformFirebase)*/

    implementation (Lib.AndroidX.coreKtx)
}
