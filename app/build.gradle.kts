import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
plugins{
    id ("com.android.application")
    id ("kotlin-parcelize")
    id ("kotlin-kapt")
    id ("org.jetbrains.kotlin.android")
    id ("com.google.gms.google-services")
   // id("com.google.devtools.ksp")
}

android {
    namespace = "com.gevcorst.popular_movies_in_theaters"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.gevcorst.popular_movies_in_theaters"
        minSdk = 24
        targetSdk = 35
        versionCode = 13
        versionName = "13.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val  theMovieAPIKey = gradleLocalProperties(  rootDir,providers).getProperty("THE_MOVIE_ORG_APIKEY")
        buildConfigField ("String", "THEMOVIEDB_API_KEY",theMovieAPIKey)
        val  theMovieDbBearer  = gradleLocalProperties(  rootDir,providers).getProperty("THE_MOVIE_DB_BEARER")
        buildConfigField( "String", "THEMOVIEDB_BEARER",theMovieDbBearer)
        val  playFingerPrint = gradleLocalProperties(  rootDir,providers).getProperty("PLAY_FINGER_PRINT")
        buildConfigField ("String", "GOOGLE_PLAY_FINGERPRINT",playFingerPrint)
    }
    buildTypes {
        release {
            isDebuggable  = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility= JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    //noinspection GradleCompatible
    implementation (libs.androidx.appcompat)
    implementation (libs.androidx.constraintlayout)
    implementation (libs.androidx.appcompat)
    implementation (libs.material)
    implementation (libs.androidx.exifinterface)

    //lifecycle
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx.v287)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.activity.ktx)
    //coroutines
    implementation(libs.kotlinx.coroutines)
    //okhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //moshi
    implementation(libs.moshi)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.core.ktx.v1150)
    implementation(libs.moshi)
    implementation(libs.converter.moshi)
    implementation(libs.gson)
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.moshi.kotlin)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)
    //FAB dependencies
    androidTestImplementation (libs.androidx.runner)
    androidTestImplementation (libs.androidx.espresso.core)
    implementation (libs.androidx.recyclerview)
    implementation (libs.androidx.vectordrawable.animated)
    //glide
    implementation (libs.glide)
    implementation(libs.coil.compose)
    //firebase
    implementation(platform(libs.firebase.bom))
    implementation (libs.arch.extensions)
    implementation (libs.viewmodel)
    implementation (libs.runtime)
    //room
    implementation (libs.androidx.lifecycle.extensions)
    kapt (libs.androidx.lifecycle.common.java8)
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)
    implementation (libs.kotlinx.serialization.json)

    //ksp ("androidx.room:room-compiler:2.6.1")
    implementation (libs.androidx.room.runtime)
    implementation (libs.androidx.room.ktx)
    implementation (libs.kotlinx.serialization.json)
    testImplementation (libs.junit)
}
