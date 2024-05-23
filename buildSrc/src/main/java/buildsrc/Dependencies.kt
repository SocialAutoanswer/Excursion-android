package buildsrc

import org.gradle.api.JavaVersion

object Versions {
    const val minSdk = 26
    const val compileSdk = 34
    const val targetSdk = 34
    const val kotlin = "1.4.20"
    val compatibility = JavaVersion.VERSION_1_8 // must be
    const val jvmTarget = "1.8"                 // the same
    const val versionCode = 1
    const val versionName = "1.0"

    object AndroidX {
        const val core = "1.9.0"
        const val appCompat = "1.6.1"
        const val constraintlayout = "2.1.4"
        const val fragments = "1.5.7"
        const val navigation = "2.7.7"
        const val lifeCycle = "2.2.0"
        const val splashScreen = "1.0.0"
        const val encryptedSharedPreferences = "1.0.0"
        const val paging3 = "3.0.0"
    }

    object Google {
        const val material = "1.11.0"
    }

    object Network {
        const val okhttp = "4.12.0"
        const val gson = "2.10.1"
        const val retrofit = "2.9.0"
    }

    const val dagger = "2.51"
    const val rxJava = "3.1.8"
    const val rxAndroid = "3.0.2"
    const val circleImage = "3.1.0"
    const val delegateAdapter = "4.2"
    const val threetenabp = "1.0.3"
    const val glide = "4.16.0"
}

object Libs {

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.AndroidX.core}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintlayout}"
        const val fragments = "androidx.fragment:fragment-ktx:${Versions.AndroidX.fragments}"
        const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.AndroidX.navigation}"
        const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.AndroidX.navigation}"
        const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.AndroidX.lifeCycle}"
        const val splashScreen = "androidx.core:core-splashscreen:${Versions.AndroidX.splashScreen}"
        const val encryptedSharedPreferences = "androidx.security:security-crypto:${Versions.AndroidX.encryptedSharedPreferences}"
        const val paging3 = "androidx.paging:paging-runtime-ktx:${Versions.AndroidX.paging3}"
        const val rxPaging3 = "androidx.paging:paging-rxjava2:${Versions.AndroidX.paging3}"
    }

    object Google {
        const val material = "com.google.android.material:material:${Versions.Google.material}"
    }

    object Network {
        const val ohttp = "com.squareup.okhttp3:okhttp" //does not need version
        const val okhttpLogInter = "com.squareup.okhttp3:logging-interceptor" //does not need version
        const val okhttpBom = "com.squareup.okhttp3:okhttp-bom:${Versions.Network.okhttp}"

        const val gson = "com.google.code.gson:gson:${Versions.Network.gson}"

        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.Network.retrofit}"
        const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.Network.retrofit}"
        const val retrofitRxJava3 = "com.squareup.retrofit2:adapter-rxjava3:${Versions.Network.retrofit}"
    }

    object DI {
        const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }

    object RxJava {
        const val rxJava = "io.reactivex.rxjava3:rxjava:${Versions.rxJava}"
        const val rxJavaAndroid = "io.reactivex.rxjava3:rxandroid:${Versions.rxAndroid}"
    }

    object Common {
        const val delegateAdapter = "io.github.d00mch:delegateadapter:${Versions.delegateAdapter}"
        const val circleImage = "de.hdodenhof:circleimageview:${Versions.circleImage}"
        const val threetenabp = "com.jakewharton.threetenabp:threetenabp:${Versions.threetenabp}"
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
        const val glideOkHttpIntegration = "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"
    }
}