plugins {
    `feature-module`
}

dependencies {
    api(coreCommon)
    api(coreKodein)
    api(coreStyling)
    api(localization)


    implementation(Lib.Kotlin.stdlib)

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
    implementation ("androidx.core:core-ktx:1.3.1")
    implementation ("androidx.appcompat:appcompat:1.2.0")
    testImplementation ("junit:junit:4.12")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
    
/*    testImplementation ("junit:junit:4.13")*/
    androidTestImplementation ("androidx.test.ext:junit:1.1.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.2.0")
    
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    api ("com.google.android.material:material:1.1.0")
    
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    
    implementation ("androidx.recyclerview:recyclerview:1.1.0")
    implementation ("androidx.cardview:cardview:1.0.0")

    // ViewModel)
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.2.0")
    // LiveData)
    implementation ("androidx.lifecycle:lifecycle-livedata:2.2.0")
    // Annotation processor)
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.2.0")
    
    api ("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    api ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    api ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    api ("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    api ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.2.0")
    api ("androidx.lifecycle:lifecycle-process:2.2.0")
    
    implementation ("androidx.fragment:fragment-ktx:1.3.0-alpha06")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    
/*    implementation "com.facebook.stetho:stetho:1.5.0")
    implementation "com.facebook.stetho:stetho-js-rhino:1.4.2"*/
    
    //blur for glide)
    implementation ("jp.wasabeef:glide-transformations:4.0.0")
    
    //logging interceptor)
    debugImplementation ("com.squareup.okhttp3:logging-interceptor:4.5.0-RC1")
    
    // add the Firebase SDK for Google Analytics)
    implementation ("com.google.firebase:firebase-analytics:17.4.4")
    // add SDKs for any other desired Firebase products)
    // https://firebase.google.com/docs/android/setup#available-libraries)
    
    // Add the Firebase Crashlytics SDK.)
    implementation ("com.google.firebase:firebase-crashlytics:17.1.1")
    
    // Add the Firebase RemoteConfig.)
    implementation ("com.google.firebase:firebase-config-ktx:19.2.0")
    
    // Add the SDK for Firebase Cloud Messaging)
    implementation ("com.google.firebase:firebase-messaging:20.2.4")
    implementation ("com.google.firebase:firebase-messaging-directboot:20.2.4")
    
    // Add the SDK for Firebase Dynamic Links)
    implementation ("com.google.firebase:firebase-dynamic-links-ktx:19.1.0")

// Places KTX
//implementation "com.google.places.android:places-v3-ktx:0.2.2"
    implementation (project(":data:repository"))

}