plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.afinal"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.afinal"
        minSdk = 36
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

tasks.register("fixDuplicates") {
    doLast {
        delete(
            "src/main/res/drawable/apple_pay.xml",
            "src/main/res/drawable/paypal.xml",
            "src/main/res/drawable/visa.xml",
            "src/main/res/drawable/background_furniture.xml",
            "src/main/res/drawable/background_furniture.jpeg",
            "src/main/res/drawable/beedroom.xml",
            "src/main/res/drawable/chair.xml",
            "src/main/res/drawable/kitchen.xml",
            "src/main/res/drawable/livingroom.xml"
        )
    }
}
