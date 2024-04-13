import buildsrc.Libs
import buildsrc.Versions

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "ru.exursion"
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "ru.exursion"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = Versions.versionCode
        versionName = Versions.versionName

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
        sourceCompatibility = Versions.compatibility
        targetCompatibility = Versions.compatibility
    }
    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.constraintlayout)
    implementation(Libs.AndroidX.fragments)
    implementation(Libs.AndroidX.navigationFragmentKtx)
    implementation(Libs.AndroidX.navigationUiKtx)
    implementation(Libs.AndroidX.splashScreen)
    implementation(Libs.Google.material)

    implementation(Libs.Network.ohttp)
    implementation(platform(Libs.Network.okhttpBom))
    implementation(Libs.Network.okhttpLogInter)

    implementation(Libs.Network.gson)

    implementation(Libs.Network.retrofit)
    implementation(Libs.Network.retrofitGson)
    implementation(Libs.Network.retrofitRxJava3)

    implementation(Libs.Common.delegateAdapter)
    implementation(Libs.Common.circleImage)
    implementation(Libs.Common.carbon)

    implementation(Libs.DI.dagger)
    kapt(Libs.DI.daggerCompiler)

    implementation(Libs.RxJava.rxJava)
    implementation(Libs.RxJava.rxJavaAndroid)
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}