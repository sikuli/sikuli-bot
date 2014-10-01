sikuli-bot
==========

# Build

	mvn package

# Run

Remote

	./bot-remote.sh

Slides

	./bot-slides.sh


# Connect to an Android device wirelessly

The basic idea is to use the Android Debug Bridge (ADB) tool to capture screen images. Even though ADB is typically run via an USB cable,  we want to run ADB wireless. To do so, we will use the app [ADB wireless by Henry](https://play.google.com/store/apps/details?id=za.co.henry.hsu.adbwirelessbyhenry&hl=en), a simple and magical tool that gets it done without rooting the device.

## Steps

Install [ADB wireless by Henry](https://play.google.com/store/apps/details?id=za.co.henry.hsu.adbwirelessbyhenry&hl=en) on the Android device you wish to test

Connect the host computer and the Android device to the same local wireless network

Connect the Android device to the host computer via an USB cable

On the host computer, run

	adb tcpip 5555	

Find out the IP of the Android device

Suppose the IP address of the Android device is 192.168.1.34, on the host computer, run

	adb connect 192.168.1.34

It should say

	connected to 192.168.1.34:5555

Disconnect the Android device from the computer (i.e., unplug the USB cable).

To test if we can do a screen capture, run

	adb shell screencap -p | perl -pe 's/\x0D\x0A/\x0A/g' > screen.png

If successful, the content of the screen should be saved in screen.png. Open it by

	open screen.png

# Connect to a Makerbot Replicator II



