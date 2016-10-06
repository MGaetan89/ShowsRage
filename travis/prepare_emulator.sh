#!/usr/bin/env bash

if [[ $ANDROID_ABI ]]; then
	echo no | android create avd --force -n test -t $ANDROID_TARGET --abi $ANDROID_ABI
	sed -i.bak 's/hw.ramSize=512/hw.ramSize=1536/' $HOME/.android/avd/test.avd/config.ini
	sed -i.bak 's/vm.heapSize=48/vm.heapSize=64/' $HOME/.android/avd/test.avd/config.ini
	emulator -avd test -no-audio -no-window &
	android-wait-for-emulator
	adb shell input keyevent 82 &
fi
