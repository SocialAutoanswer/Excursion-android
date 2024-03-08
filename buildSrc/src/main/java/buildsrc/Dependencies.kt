package buildsrc

import org.gradle.api.JavaVersion

object Versions {
    const val minSdk = 24
    const val compileSdk = 33
    const val targetSdk = 33
    const val kotlin = "1.4.20"
    val compatibility = JavaVersion.VERSION_1_8 // must be
    const val jvmTarget = "1.8"                 // the same
}

object Libs {

    object Network {

    }

    object DI {

    }

    object Test {

    }
}