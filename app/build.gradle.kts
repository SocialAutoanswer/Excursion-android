import buildsrc.Libs
import buildsrc.Versions
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

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

        val excUrl: String? = gradleLocalProperties(rootDir).getProperty("EXC_URL")
        val testAuthToken: String? = gradleLocalProperties(rootDir).getProperty("testingAuthToken")
        val supportPhoneNumber: String? = gradleLocalProperties(rootDir).getProperty("supportPhoneNumber")
        val supportTelegramUserId: String? = gradleLocalProperties(rootDir).getProperty("supportTelegramUserId")
        val mapKitApiKey: String? = gradleLocalProperties(rootDir).getProperty("MAPKIT_API_KEY")
        val searchEnabled = gradleLocalProperties(rootDir).getProperty("SEARCH_ENABLED")
        val notificationsSettingEnabled = gradleLocalProperties(rootDir).getProperty("NOTIFICATIONS_SETTINGS_ENABLED")
        val aboutSettingEnabled = gradleLocalProperties(rootDir).getProperty("ABOUT_SETTING_ENABLED")
        val reviewsHeaderEnabled = gradleLocalProperties(rootDir).getProperty("REVIEW_HEADER_ENABLED")
        val privacyLink = gradleLocalProperties(rootDir).getProperty("PRIVACY_LINK")

        buildConfigField("String", "EXC_URL", "\"$excUrl\"")
        buildConfigField("String", "AUTH_TOKEN", testAuthToken ?: "\"\"")
        buildConfigField("String", "SUPPORT_PHONE_NUMBER", "\"$supportPhoneNumber\"")
        buildConfigField("String", "SUPPORT_TELEGRAM_USER_ID", "\"$supportTelegramUserId\"")
        buildConfigField("String", "MAPKIT_API_KEY", "\"$mapKitApiKey\"")
        buildConfigField("boolean", "SEARCH_ENABLED", searchEnabled)
        buildConfigField("boolean", "NOTIFICATIONS_SETTINGS_ENABLED", notificationsSettingEnabled)
        buildConfigField("boolean", "ABOUT_SETTING_ENABLED", aboutSettingEnabled)
        buildConfigField("boolean", "REVIEW_HEADER_ENABLED", reviewsHeaderEnabled)
        buildConfigField("String", "PRIVACY_LINK", "\"$privacyLink\"")
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
        buildConfig = true
    }
}

dependencies {

    implementation(project(":android-kit"))

    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.constraintlayout)
    implementation(Libs.AndroidX.fragments)
    implementation(Libs.AndroidX.navigationFragmentKtx)
    implementation(Libs.AndroidX.navigationUiKtx)
    implementation(Libs.AndroidX.splashScreen)
    implementation(Libs.AndroidX.encryptedSharedPreferences)
    implementation(Libs.AndroidX.paging3)
    implementation(Libs.AndroidX.rxPaging3)
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

    implementation(Libs.Common.glide)
    kapt(Libs.Common.glideCompiler)
    implementation(Libs.Common.glideOkHttpIntegration)

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.github.fuzz-productions:RatingBar:1.0.6")


    implementation(Libs.DI.dagger)
    kapt(Libs.DI.daggerCompiler)

    implementation(Libs.RxJava.rxJava)
    implementation(Libs.RxJava.rxJavaAndroid)

    implementation(Libs.Common.codeEditText)

    implementation(Libs.Common.threetenabp)

    implementation(Libs.AndroidX.swipeRefreshLayout)

    implementation(Libs.Common.yandexMapKit)
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