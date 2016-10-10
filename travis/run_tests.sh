#!/usr/bin/env bash

./gradlew build testDebug

if [[ $ANDROID_ABI ]]; then
	travis_wait gradlew connectedDebugAndroidTest
fi
