object Android {
    private const val GRADLE_PLUGIN_VERSION = "3.5.0-alpha04"

    const val GRADLE_PLUGIN = "com.android.tools.build:gradle:$GRADLE_PLUGIN_VERSION"
}

object Kotlin {
    private const val VERSION = "1.3.11"
    private const val COROUTINES_VERSION = "1.1.0"

    const val GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"
    const val STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$VERSION"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINES_VERSION"
}

object JUnit {
    private const val JUNIT_VERSION = "4.12"

    const val JUNIT = "junit:junit:$JUNIT_VERSION"
}

object AndroidXTest {
    private const val TEST_CORE_KTX_VERSION = "1.1.0"
    private const val TEST_RUNNER_VERSION = "1.1.1"
    private const val TEST_EXT_JUNIT_VERSION = "1.1.0"

    const val CORE_KTX = "androidx.test:core-ktx:$TEST_CORE_KTX_VERSION"
    const val RUNNER = "androidx.test:runner:$TEST_RUNNER_VERSION"
    const val EXT_JUNIT = "androidx.test.ext:junit:$TEST_EXT_JUNIT_VERSION"
}

object Espresso {
    private const val VERSION = "3.1.1"

    const val CORE = "androidx.test.espresso:espresso-core:$VERSION"
}

object Material {
    private const val VERSION = "1.1.0-alpha03"

    const val MATERIAL = "com.google.android.material:material:$VERSION"
}

object Jetpack {
    private const val ACTIVITY_KTX_VERSION = "1.0.0-alpha04"
    private const val APPCOMPAT_VERSION = "1.1.0-alpha01"
    private const val CORE_KTX_VERSION = "1.1.0-alpha04"
    private const val FRAGMENT_KTX_VERSION = "1.1.0-alpha03"
    private const val PREFERENCE_KTX_VERSION = "1.1.0-alpha02"
    private const val RECYCLERVIEW_VERSION = "1.1.0-alpha01"
    private const val CONSTRAINT_LAYOUT_VERSION = "2.0.0-alpha3"

    const val ACTIVITY_KTX = "androidx.activity:activity-ktx:$ACTIVITY_KTX_VERSION"
    const val APPCOMPAT = "androidx.appcompat:appcompat:$APPCOMPAT_VERSION"
    const val CORE_KTX = "androidx.core:core-ktx:$CORE_KTX_VERSION"
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:$FRAGMENT_KTX_VERSION"
    const val PREFERENCE_KTX = "androidx.preference:preference-ktx:$PREFERENCE_KTX_VERSION"
    const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:$RECYCLERVIEW_VERSION"
    const val RECYCLERVIEW_SELECTION = "androidx.recyclerview:recyclerview-selection:$RECYCLERVIEW_VERSION"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:$CONSTRAINT_LAYOUT_VERSION"
}

object Lifecycle {
    private const val VERSION = "2.1.0-alpha02"

    const val EXTENSIONS = "androidx.lifecycle:lifecycle-extensions:$VERSION"
    const val LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:$VERSION"
    const val VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:$VERSION"
    const val COMMON_JAVA8 = "androidx.lifecycle:lifecycle-common-java8:$VERSION"
}

object Navigation {
    private const val VERSION = "1.0.0-beta01"

    const val FRAGMENT_KTX = "android.arch.navigation:navigation-fragment-ktx:$VERSION"
    const val UI_KTX = "android.arch.navigation:navigation-ui-ktx:$VERSION"
    const val SAFE_ARGS_GRADLE_PLUGIN = "android.arch.navigation:navigation-safe-args-gradle-plugin:$VERSION"
}

object Dagger {
    private const val VERSION = "2.21"

    const val DAGGER = "com.google.dagger:dagger:$VERSION"
    const val ANDROID_SUPPORT = "com.google.dagger:dagger-android-support:$VERSION"
    const val COMPILER = "com.google.dagger:dagger-compiler:$VERSION"
    const val ANDROID_PROCESSOR = "com.google.dagger:dagger-android-processor:$VERSION"
}

object LeakCanary {
    private const val VERSION = "1.6.3"

    const val ANDROID = "com.squareup.leakcanary:leakcanary-android:$VERSION"
    const val ANDROID_NO_OP = "com.squareup.leakcanary:leakcanary-android-no-op:$VERSION"
}

object Moshi {
    private const val VERSION = "1.8.0"

    const val MOSHI = "com.squareup.moshi:moshi:$VERSION"
    const val KOTLIN_CODEGEN = "com.squareup.moshi:moshi-kotlin-codegen:$VERSION"
}

object Retrofit {
    private const val VERSION = "2.5.1-SNAPSHOT"
    private const val COROUTINES_ADAPTER_VERSION = "0.9.2"

    const val RETROFIT = "com.squareup.retrofit2:retrofit:$VERSION"
    const val CONVERTER_SCALARS = "com.squareup.retrofit2:converter-scalars:$VERSION"
    const val CONVERTER_MOSHI = "com.squareup.retrofit2:converter-moshi:$VERSION"
    const val ADAPTER_COROUTINES =
            "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:$COROUTINES_ADAPTER_VERSION"
}

object OkHttp {
    private const val VERSION = "3.13.1"

    const val OKHTTP = "com.squareup.okhttp3:okhttp:$VERSION"
}

object Gander {
    private const val VERSION = "1.4.0"

    const val GANDER = "com.ashokvarma.android:gander:$VERSION"
    const val GANDER_NO_OP = "com.ashokvarma.android:gander-no-op:$VERSION"
}

object Arrow {
    private const val VERSION = "0.8.2"

    const val CORE = "io.arrow-kt:arrow-core:$VERSION"
}

object MaterialDialogs {
    private const val VERSION = "2.0.0-rc11"

    const val CORE = "com.afollestad.material-dialogs:core:$VERSION"
    const val FILES = "com.afollestad.material-dialogs:files:$VERSION"
    const val INPUT = "com.afollestad.material-dialogs:input:$VERSION"
}

object Dexter {
    private const val VERSION = "5.0.0"

    const val DEXTER = "com.karumi:dexter:$VERSION"
}
