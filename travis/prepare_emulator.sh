#!/usr/bin/env bash

if [[ $ANDROID_ABI ]]; then
	echo no | android create avd --force -n test -t $ANDROID_TARGET --abi $ANDROID_ABI
	emulator -avd test -no-audio -no-window &
	android-wait-for-emulator
	adb shell input keyevent 82 &
fi
