plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.xender"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.xender"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("com.github.bumptech.glide:glide:5.0.0-rc01")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.2")
    implementation ("androidx.multidex:multidex:2.0.1")

    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
    implementation ("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")



}