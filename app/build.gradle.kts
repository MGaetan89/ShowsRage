import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension
import java.util.Properties

plugins {
    id("com.android.application")
    id("com.vanniktech.android.junit.jacoco")
    id("io.fabric")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("realm-android")
}

val properties = Properties()
if (rootProject.file("local.properties").exists()) {
    properties.load(rootProject.file("local.properties").inputStream())
}

android {
    compileSdkVersion(Versions.compileSdk)

    defaultConfig {
        applicationId = "com.mgaetan89.showsrage"
        versionCode = 38
        versionName = "1.7"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        resConfigs("en", "fr")
    }

    signingConfigs {
        create("release") {
            keyAlias = properties.getProperty("signing.keyAlias", "")
            keyPassword = properties.getProperty("signing.keyPassword", "")
            storeFile = file(properties.getProperty("signing.storeFile", "-"))
            storePassword = properties.getProperty("signing.storePassword", "")
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".dev"
            isTestCoverageEnabled = true
        }

        getByName("release") {
            isMinifyEnabled = false

            if (file(signingConfigs.getByName(name).storeFile).exists()) {
                signingConfig = signingConfigs.getByName(name)
            }
        }
    }

    sourceSets {
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }

    lintOptions {
        isAbortOnError = false
    }

    testOptions {
        animationsDisabled = true

        unitTests.apply {
            isReturnDefaultValues = true
        }
    }
}

androidExtensions {
    // Workaround for https://github.com/gradle/kotlin-dsl/issues/644
    configure(delegateClosureOf<AndroidExtensionsExtension> {
        isExperimental = true
    })
}

junitJacoco {
    jacocoVersion = Versions.jacoco
    isIncludeNoLocationClasses = true
}

realm {
    setKotlinExtensionsEnabled(false)
}

tasks.withType<Test> {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
    }
}

dependencies {
    implementation(Dependencies.appCompat)
    implementation(Dependencies.cardView)
    implementation(Dependencies.customTabs)
    implementation(Dependencies.design)
    implementation(Dependencies.mediaRouter)
    implementation(Dependencies.palette)
    implementation(Dependencies.recyclerView)
    implementation(Dependencies.supportAnnotations)
    implementation(Dependencies.supportV4)
    implementation(Dependencies.supportVectorDrawable)
    implementation(Dependencies.crashlytics)
    implementation(Dependencies.firebaseJobDispatcher)
    implementation(Dependencies.fastscroll)
    implementation(Dependencies.glide)
    implementation(Dependencies.glideOkHttp)
    implementation(Dependencies.playServicesCast)
    implementation(Dependencies.firebaseAnalytics)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.okHttpUrlConnection)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.preferences)
    implementation(Dependencies.realmAdapters)
    implementation(Dependencies.kotlin)

    testImplementation(Dependencies.assertJAndroid)
    testImplementation(Dependencies.jUnit)
    testImplementation(Dependencies.mockito)

    androidTestImplementation(Dependencies.supportAnnotations)
    androidTestImplementation(Dependencies.supportTestLibraryRules)
    androidTestImplementation(Dependencies.supportTestLibraryRunner)
    androidTestImplementation(Dependencies.assertJAndroid)
    androidTestImplementation(Dependencies.jUnit)
}

apply(mapOf("plugin" to "com.google.gms.google-services"))
