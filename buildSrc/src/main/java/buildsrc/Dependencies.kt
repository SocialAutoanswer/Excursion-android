package buildsrc

import org.gradle.api.JavaVersion

object Versions {
    const val minSdk = 24
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
    }

    object Google {
        const val material = "1.11.0"
    }
}

object Libs {

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.AndroidX.core}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintlayout}"
    }

    object Google {
        const val material = "com.google.android.material:material:${Versions.Google.material}"
    }

    object Network {

    }

    object DI {

    }

    object Test {

    }
}