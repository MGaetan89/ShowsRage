#!/usr/bin/env bash

./gradlew build testDebug

if [[ $ANDROID_ABI ]]; then
	./gradlew connectedDebugAndroidTest
fi
