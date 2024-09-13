import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    //hilt 추가 내용
    alias(libs.plugins.dagger.hilt.android)

    kotlin("plugin.serialization") version "1.5.0"

    id("kotlin-parcelize")
    id("kotlin-kapt")


    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.blackcows.butakaeyak"
    compileSdk = 34

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.blackcows.butakaeyak"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.blackcows.butakaeyak.HiltTestRunner"

        buildConfigField(
            "String",
            "DRUG_INFO_KEY",
            properties.getProperty("drug_info_key")
        )
        buildConfigField(
            "String",
            "NATIVE_APP_KEY",
            properties.getProperty("native_app_key")
        )
        buildConfigField(
            "String",
            "REST_API_KEY",
            properties.getProperty("rest_api_key")
        )
        buildConfigField(
            "String",
            "ALGORIA_APP_ID",
            properties.getProperty("algoria_app_id")
        )
        buildConfigField(
            "String",
            "ALGORIA_SEARCH_KEY",
            properties.getProperty("algoria_search_key")
        )
        buildConfigField("String",
            "NAVER_CLIENT_ID",
            properties.getProperty("naver_client_id")
        )
        buildConfigField("String",
            "NAVER_CLIENT_SECRET",
            properties.getProperty("naver_client_secret")
        )
        buildConfigField("String",
            "PHARMACY_LIST_INFO_KEY",
            properties.getProperty("pharmacy_list_info_key")
        )

        manifestPlaceholders["kakao_native_app_key"] =
            properties.getProperty("kakao_native_app_key")

        buildConfigField(
            "String",
            "NAVER_CLIENT_ID",
            properties.getProperty("naver_client_id")
        )
        buildConfigField(
            "String",
            "NAVER_CLIENT_SECRET",
            properties.getProperty("naver_client_secret")
        )
        buildConfigField(
            "String",
            "PHARMACY_LIST_INFO_KEY",
            properties.getProperty("pharmacy_list_info_key")
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.activity)
    implementation(libs.firebase.firestore)

    implementation(libs.androidx.runner)

    implementation(libs.androidx.junit.ktx)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    val camerax_version = "1.5.0-alpha01"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.runtime.android)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.glide)
    annotationProcessor("com.github.bumptech.glide:compiler:4.7.1")

    implementation(libs.androidx.lifecycle.livedata.ktx)

    //async
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
    // For Robolectric tests.
    testImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptTest("com.google.dagger:hilt-android-compiler:2.47")
    // For instrumented tests.
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    // ...with Kotlin.
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.47")

    testImplementation("org.robolectric:robolectric:4.9")

    // LiveData (optional)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx")

    // Coroutine (for StateFlow)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")

    // 카카오 지도 SDK
    implementation("com.kakao.sdk:v2-all:2.20.5")
    implementation("com.kakao.maps.open:android:2.11.9")

    implementation("com.google.android.gms:play-services-location:18.0.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")     //인증

    //Algoria
    implementation("com.algolia:algoliasearch-android:3.27.0")
    implementation("com.algolia:algoliasearch-client-kotlin:2.1.9")

    // Tikxml
    implementation("com.tickaroo.tikxml:annotation:0.8.13")
    implementation("com.tickaroo.tikxml:core:0.8.13")
    implementation("com.tickaroo.tikxml:retrofit-converter:0.8.13")
    kapt("com.tickaroo.tikxml:processor:0.8.13")

    //ML Kit
    // To recognize Korean script
    implementation ("com.google.mlkit:text-recognition-korean:16.0.1")
}