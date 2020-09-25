plugins {
    `feature-module`
}

dependencies {
    /*    // Maps 3.1.0 Beta
    implementation 'com.google.android.libraries.maps:maps:3.1.0-beta'
    // Map Utils
    implementation 'com.google.maps.android:android-maps-utils-v3:2.0.3'

    // KTX for the Maps SDK for Android V3 BETA Library
    implementation 'com.google.maps.android:maps-v3-ktx:2.1.1'
    // KTX for the Maps SDK for Android V3 BETA Utility Library
    implementation 'com.google.maps.android:maps-utils-v3-ktx:2.1.1'

    implementation 'com.google.android.gms:play-services-basement:17.4.0'
    implementation 'com.google.android.gms:play-services-base:17.4.0'

    // If you are using Places, add all of the dependencies in this section
    implementation fileTree(include: ['*.jar', '*.aar'], dir: 'libs')
    implementation files('libs/places-maps-sdk-3.1.0-beta.aar')
    //implementation name:'places-maps-sdk-3.1.0-beta', ext:'aar' (alternative)
    implementation 'com.google.android.gms:play-services-gcm:17.0.0'
    implementation 'com.google.auto.value:auto-value-annotations:1.6.2'*/

/*    // Location
    implementation 'com.google.android.gms:play-services-location:17.0.0'*/
    implementation (corePresentation)
    implementation (dataRepository)
/*    implementation (localization)*/
}