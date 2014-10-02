sikuli-bot
==========

# Build

For MacOSX

	mvn package -Dplatform.dependency=macosx-x86_64

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


# Troubleshooting

If you get an error that reads like

	/dev/tty.usbmodem1421' already in use error

First, run these to resolve the issue

	sudo mkdir /var/lock
	sudo chmod a+rw /var/lock

This is based on [this tip](http://stackoverflow.com/questions/12866572/rxtx-on-mac-serial-port-already-in-use) on Stackoverflow.


If the above step does not work, the port is greped by the MakerWare software.
so Run the following command to free the USB port in use

	sudo launchctl unload /Library/LaunchDaemons/com.makerbot.conveyor.plist
	
This command should be ran everytime you reset/reboot your system

This is based on [this tip](http://www.makerbot.com/support/replicatorg/troubleshooting/) on MakerBot ReplicatorG


If your laptop does not find any devices or unable to connect to the device with USB cable like
	
	error: device not found
	unable to connect: <your IP address>:5555

Kill adb service server and retry to connect

	adb kill-server
	adb connect <your IP addess>


