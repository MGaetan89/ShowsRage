#!/usr/bin/env bash

if [[ $ANDROID_ABI ]]; then
	EMULATOR_NAME="test_${ANDROID_TARGET}_${ANDROID_ABI}"

	echo no | android create avd -f -n $EMULATOR_NAME -t $ANDROID_TARGET -b $ANDROID_ABI -g "google_apis"
	emulator -memory 1536 -avd $EMULATOR_NAME -no-audio -no-skin -no-window -wipe-data &
	android-wait-for-emulator
	adb shell input keyevent 82 &
fi
