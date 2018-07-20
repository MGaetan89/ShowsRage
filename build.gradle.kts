// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven(Repositories.fabric)
    }

    dependencies {
        classpath(Dependencies.androidPlugin)
        classpath(Dependencies.fabricPlugin)
        classpath(Dependencies.googleServicesPlugin)
        classpath(Dependencies.jacocoPlugin)
        classpath(Dependencies.kotlinPlugin)
        classpath(Dependencies.realmPlugin)
        classpath(Dependencies.sonarQubePlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(Repositories.jitpack)
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
