#!/usr/bin/env bash

touch build/logcat.log

if [[ $ANDROID_ABI ]]; then
	echo no | android create avd --force -n test -t $ANDROID_TARGET --abi $ANDROID_ABI
	emulator -avd test -no-audio -no-window &
	android-wait-for-emulator
	adb shell input keyevent 82 &
	adb logcat -c
	adb logcat > build/logcat.log &
fi
