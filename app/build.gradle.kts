plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.madebyratik.colorgram"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.madebyratik.colorgram"
        minSdk = 21
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro"),
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.preference)
    implementation(libs.coroutines.android)
    implementation(libs.koin.android)
    implementation(libs.material)

    testImplementation(libs.junit)
}
