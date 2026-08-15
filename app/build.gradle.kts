import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use(::load)
    }
}

val releaseSigning = if (keystorePropertiesFile.exists()) {
    android.signingConfigs.create("release") {
        storeFile = keystoreProperties.getProperty("storeFile")?.let(rootProject::file)
        storePassword = keystoreProperties.getProperty("storePassword")
        keyAlias = keystoreProperties.getProperty("keyAlias")
        keyPassword = keystoreProperties.getProperty("keyPassword")
    }
} else {
    null
}

android {
    namespace = "com.madebyratik.colorgram"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.madebyratik.colorgram"
        minSdk = 21
        targetSdk = 37
        versionCode = 2
        versionName = "1.1"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = releaseSigning
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

val validateReleaseSigning = tasks.register("validateReleaseSigning") {
    group = "verification"
    description = "Validates the local release-signing configuration."

    doLast {
        check(keystorePropertiesFile.isFile) {
            "Missing keystore.properties. Copy keystore.properties.example and add the release credentials."
        }

        listOf("storeFile", "storePassword", "keyAlias", "keyPassword").forEach { property ->
            check(!keystoreProperties.getProperty(property).isNullOrBlank()) {
                "Missing '$property' in keystore.properties."
            }
        }

        val storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
        check(storeFile.isFile) {
            "Release keystore does not exist: ${storeFile.absolutePath}"
        }
    }
}

tasks.configureEach {
    if (name == "preReleaseBuild") {
        dependsOn(validateReleaseSigning)
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
