# Shared
Web based Android App written in Java with Url to QrCode and Cookie Handlers

## Features
The Ui consists of a simple ConstraintLayout with components :
- webView -- loads the url data (acts as web browser)
- inputUrl -- targets url sources | search words with www.google.fr
- linearLayout -- horizontal layout with 5 buttons :
  - buttonUrl -- acts like a **Go load** that inputUrl content | refresh
  - buttonBack -- acts like **Go back** to the original start page
  - buttonShared -- uses **api.qrserver.com** with inputUrl to generate QrCode
  - buttonCookie -- uses **webkit cookie manager** to erase session cookies
  - buttonExit -- simple exit the activity
 - ui is responsive and adapts to background sleep and orientation rotation modes

## Supported devices
Design with backward-compatibilty in mind :
- minimum Sdk is 21 (Android 5.0 -- december 2014)
- target Sdk is 31 (Android 12.0 -- august 2021)
- should also work for higher Sdk numbers (Android 13, 14, 15 ...) 

## Setup prerequisites
Should work on windows, mac os and other linux distros.<br>
Original work environment used :
- Linux Mint 22.2 x86_64 (host os)
- Android Studio Bumblebee 2021.1.1 | built on jan 19, 2022 (ide)
  - requires debug enabled on android
- Real test devices | 6.5 inch phone android 12 (adb) with usb cable or wifi debug
- Remote testing (scrcpy)

## Test package (apk)
Install without internet to avoid Google Play Scanner.<br>
This program is built without background activity trackers.<br>
The shared code source is whats inside the **shared.apk**.<br>
The app does not mess with your personal files.

## Test results
Some screenshots below.

## License
MIT -- feel free to checkout | borrow features | fork<br>

Using the foundata qrserver online api service : <br>
[foundata](https://github.com/foundata)<br>
[QR code generator](https://goqr.me/api/)<br>

Copyright (c) 2026 alexander14k28@gmail.com

See [LICENSE](LICENSE) for the license governing this project.
