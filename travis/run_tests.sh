#!/usr/bin/env bash

if [[ $ANDROID_ABI ]]; then
	./gradlew build testDebug connectedDebugAndroidTest
else
	./gradlew build testDebug
fi
