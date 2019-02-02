plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC12"
}

android {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "com.redridgeapps.remoteforqbittorrent"

        minSdkVersion(21)
        targetSdkVersion(28)

        versionCode = 1
        versionName = "0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding {
        isEnabled = true
    }
}

androidExtensions {
    isExperimental = true
}

detekt {
    input = files("$rootDir")
    config = files("$rootDir/detekt-config.yml")
    filters = ".*/resources/.*,.*/build/.*"
    baseline = file("$rootDir/baseline.xml")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    implementation(Kotlin.STDLIB)
    implementation(Kotlin.COROUTINES)

    // Testing
    testImplementation(JUnit.JUNIT)
    androidTestImplementation(AndroidXTest.CORE_KTX)
    androidTestImplementation(AndroidXTest.RUNNER)
    androidTestImplementation(AndroidXTest.EXT_JUNIT)
    androidTestImplementation(Espresso.CORE)

    // Material
    implementation(Material.MATERIAL)

    // Jetpack
    implementation(Jetpack.ACTIVITY_KTX)
    implementation(Jetpack.APPCOMPAT)
    implementation(Jetpack.CORE_KTX)
    implementation(Jetpack.FRAGMENT_KTX)
    implementation(Jetpack.PREFERENCE_KTX)
    implementation(Jetpack.RECYCLERVIEW)
    implementation(Jetpack.RECYCLERVIEW_SELECTION)
    implementation(Jetpack.CONSTRAINT_LAYOUT)

    // Android Architecture Components
    implementation(Lifecycle.EXTENSIONS)
    implementation(Lifecycle.LIVEDATA_KTX)
    implementation(Lifecycle.VIEWMODEL_KTX)
    implementation(Lifecycle.COMMON_JAVA8)
    implementation(Navigation.FRAGMENT_KTX)
    implementation(Navigation.UI_KTX)

    // Dagger
    implementation(Dagger.DAGGER)
    implementation(Dagger.ANDROID_SUPPORT)
    kapt(Dagger.COMPILER)
    kapt(Dagger.ANDROID_PROCESSOR)

    // LeakCanary
    debugImplementation(LeakCanary.ANDROID)
    releaseImplementation(LeakCanary.ANDROID_NO_OP)

    // Moshi
    implementation(Moshi.MOSHI)
    kapt(Moshi.KOTLIN_CODEGEN)

    // Retrofit + ( Converters and Adapters )
    implementation(Retrofit.RETROFIT)
    implementation(Retrofit.CONVERTER_SCALARS)
    implementation(Retrofit.CONVERTER_MOSHI)
    implementation(Retrofit.ADAPTER_COROUTINES)

    // OkHttp
    implementation(OkHttp.OKHTTP)

    // Gander
    debugImplementation(Gander.GANDER)
    releaseImplementation(Gander.GANDER_NO_OP)

    // Arrow
    implementation(Arrow.CORE)

    // Material Dialogs
    implementation(MaterialDialogs.CORE)
    implementation(MaterialDialogs.FILES)
    implementation(MaterialDialogs.INPUT)

    // Dexter
    implementation(Dexter.DEXTER)
}