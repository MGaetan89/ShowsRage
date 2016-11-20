#!/usr/bin/env bash

if [[ $ANDROID_ABI ]]; then
	echo no | android create avd --force -n test -t $ANDROID_TARGET --abi $ANDROID_ABI
	emulator -memory 1536 -avd test -no-audio -no-skin -no-window -wipe-data &
	android-wait-for-emulator
	adb shell input keyevent 82 &
fi
