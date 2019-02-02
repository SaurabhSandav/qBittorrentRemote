import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter {
            content {
                // AGP
                includeGroup("org.jetbrains.trove4j")
            }
        }
    }

    dependencies {
        classpath(Android.GRADLE_PLUGIN)
        classpath(Kotlin.GRADLE_PLUGIN)
        classpath(Navigation.SAFE_ARGS_GRADLE_PLUGIN)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter {
            content {

                // LeakCanary
                includeGroup("org.jetbrains.trove4j")

                // Detekt
                includeGroup("io.gitlab.arturbosch.detekt")
                includeGroup("com.beust")

                // Material Dialogs
                includeGroup("com.afollestad.material-dialogs")
            }
        }

        // Retrofit Snapshots
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
            content { includeGroup("com.squareup.retrofit2") }
        }
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions.freeCompilerArgs += listOf("-progressive", "-Xnew-inference")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
