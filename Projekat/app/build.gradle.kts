import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.symbol.processing)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.realm.kotlin)
    kotlin("kapt")
    id("org.jetbrains.kotlinx.kover") version "0.9.1"

    // firebase-authentication
    id("com.google.gms.google-services")
}

android {
    namespace = "rs.ac.bg.etf.projekat"
    compileSdk = 35

    defaultConfig {
        applicationId = "rs.ac.bg.etf.projekat"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
     //   testInstrumentationRunner = "dagger.hilt.android.testing.HiltTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.protolite.well.known.types)
    implementation(libs.material)
    //implementation(libs.androidx.compose.testing)
    implementation(libs.androidx.animation.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.ui.test.junit4.android)
    implementation(libs.androidx.runner)
    implementation(libs.androidthings)
    testImplementation(libs.junit)
    //androidTestImplementation(libs.androidx.junit)
    //androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.compose.hilt)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp3.logging.interceptor)

    // add for hilt di
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler) // add the plugin also!

    implementation(libs.realm.library)
    //implementation(libs.io.realm.gradle.plugin)
    implementation(kotlin("reflect"))


    implementation("androidx.compose.material:material:1.5.4")

    //za mapu
    implementation("org.osmdroid:osmdroid-android:6.1.11")

    // swipe-abilnost
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")



    // Compose test
    // Hilt za testiranje (ako koristiš Hilt)
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.52")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.8.2")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.8.2")

    //testImplementation("junit:junit:4.13.2")
    //testImplementation("org.robolectric:robolectric:4.13")


    testImplementation("io.mockk:mockk:1.13.10")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    // work-manager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // firebase-authentication
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.auth.ktx)
    implementation (libs.androidx.credentials)
    implementation (libs.androidx.credentials.play.services.auth)
    implementation (libs.googleid)
    implementation(libs.facebook.android.sdk)

    // UI tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
