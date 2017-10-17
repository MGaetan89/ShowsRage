#!/usr/bin/env bash

if [[ $ANDROID_ABI ]]; then
	EMULATOR_NAME="test_${ANDROID_TARGET}_${ANDROID_ABI}"

	echo no | android create avd --force -n $EMULATOR_NAME -k "system-images;${ANDROID_TARGET};google_apis;${ANDROID_ABI}"
	emulator -memory 1536 -avd $EMULATOR_NAME -no-audio -no-skin -no-window -wipe-data &
	android-wait-for-emulator
	adb shell input keyevent 82 &
fi
