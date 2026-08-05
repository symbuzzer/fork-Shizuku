# Shizuku-fork

The new and easy-to-use Shizuku variant.

## What is Shizuku?

An Android app that allows other apps to use system-level APIs that require adb/root privileges.

## Screenshots

<img width="108" height="234" alt="main-stopped" src="https://github.com/user-attachments/assets/4cdbb5ac-55e2-47af-891c-2bd86165f346" />
<img width="108" height="234" alt="main-running" src="https://github.com/user-attachments/assets/6e3ef91c-78e9-48fc-b70a-18bd85ee5a84" />
<img width="108" height="290" alt="settings" src="https://github.com/user-attachments/assets/1253fc04-4da3-403e-a2c1-c0df61abffd0" />

## Disclaimer

This is a **FORK** of [unofficial Shizuku fork by thedjchi](https://github.com/thedjchi/Shizuku).
- If you are looking for the original Shizuku, please visit the [RikkaApps/Shizuku](https://github.com/RikkaApps/Shizuku) repository.
- If you are looking for the Shizuku fork that served as the base for this variant, please visit the [thedjchi/Shizuku](https://github.com/thedjchi/Shizuku) repository.

*Note: This is for my own use. Use it at your own risk.*

## Download

Get the [latest](https://github.com/symbuzzer/fork-Shizuku/releases/latest) version.  
All versions and changelogs are distributed via [GitHub Releases](https://github.com/symbuzzer/fork-Shizuku/releases).  

## How to install

1- **Remove all Shizuku variants before installing and reboot the device.** Because, all of them uses same package names.  
2- **Disable Play Protect before installation.** Because, offical Shizuku is already uploaded to Play Store and it has same package name but different signature. So Play Protect detects all variants of Shizuku as malicious.  
3- **Download and install the [latest apk](https://github.com/symbuzzer/fork-Shizuku/releases/latest/download/Shizuku-fork.apk)**  
4- **Follow on-screen instructions.**  

*Note: During the first setup and each reboot, the device should only be connected to a Wi-Fi network once.*

## Added features by symbuzzer

This variant of thedjchi's Shizuku includes some extra features over the unoffical thedjchi's forked version, such as:
* **Fewer options, much easier to use:** Many options/features have been hidden and will only be shown when needed. Additionally, those I considered redundant have been removed entirely.
* **Initial setup process has been made more automated:** Instead of displaying unnecessary notifications/warnings, processes are now automated. Additionally, the "Pairing" button has been completely removed. All processes can now be completed using only the "Start" button.
* **Added hide/show advanced Shizuku features and settings option:** This allows you to hide some features and settings that aren't used very often and can be confusing.
* **All external links that I deemed unnecessary have been removed:** Instructions in the application interface are quite adequate.
* **Improved user experience:** "Stop" button and "About" section have been moved to more native locations.
* **Integrated language selection feature into native Android Settings:** Instead of changing the language from the app's own settings, you can change it in a more integrated way through Android's settings.
* **Reimplemented in-app update feature:** Now updates can be downloaded and installed directly within the app.
* **Removed "Stealth mode" feature to allow the application to be published on any FOSS Repository in future.**
* **Fixed Turkish and Portuguese translations.**

## Added features by thedjchi

thedjchi's original Shizuku version includes some extra features over the original version, such as:
* **More robust "start on boot":** waits for a Wi-Fi connection before starting the Shizuku service
* **TCP mode:** (i.e., the `adb tcpip` command) once Shizuku successfully starts with Wi-Fi after a reboot, you can stop/restart Shizuku without a Wi-Fi connection!
* **Watchdog service:** automatically restarts Shizuku if it stops unexpectedly, and can alert you of crashes/potential fixes
* **Start/stop intents:** toggle Shizuku on-demand using automation apps (e.g., Tasker, MacroDroid, Automate)
* ~~**[BETA] Stealth mode:** hide Shizuku from other apps that don't work when Shizuku is installed~~
* ~~**[BETA] In-app updates:** option to automatically check for new updates, and can automatically download/install the latest version from GitHub~~
* **Android/Google TV and VR headset support:** UI is now compatible with D-Pad remotes, all TVs are supported (including Android 14+ TVs that require pairing), and the multi-window pairing dialog is toggleable in settings for VR headsets
* **MediaTek support:** fixes a critical bug in the original v13.6.0 which prevented Shizuku from working on MediaTek devices
* And more!

## Requirements

**Minimum Version: Android 7+**
- **Root mode:** Requires a rooted device
- **Wireless Debugging mode:** Works on Android 11+ and all Android TVs
- **PC mode:** Works on all devices
- **Start on boot:** Available only when using Wireless Debugging or Root mode

## Privacy

Shizuku takes user privacy very seriously.

* No tracking or analytics
* No telemetry
* No proprietary libraries
* No Google Play Services
* Open-source codebase
* Reproducible builds
* Internet access is only used for wireless debugging connections
* Only required permissions are declared

### Permissions

* **INTERNET:** required for the wireless debugging start mode to work.
* **ACCESS_NETWORK_STATE:** used to determine when Wi-Fi is available for background start via wireless debugging
* **POST_NOTIFICATIONS:** required for pairing notification and other alerts
* **RECEIVE_BOOT_COMPLETED:** required for start on boot
* **FOREGROUND_SERVICE:** prevents watchdog from being killed
* **REQUEST_IGNORE_BATTERY_OPTIMIZATIONS:** prevents start on boot and watchdog services from being killed
* **REQUEST_INSTALL_PACKAGES:** allows the app to install downloaded updates.
* **WRITE_SECURE_SETTINGS:** used to toggle USB and wireless debugging in the background when starting/stopping Shizuku
* **NEARBY_WIFI_DEVICES:** required for connecting device itself via wireless ADB

## Translations
Supported languages for now:  
- EN
- TR
- PT *(thanks to [marciozomb13](https://github.com/symbuzzer/fork-Shizuku/issues?q=is%3Apr+author%3Amarciozomb13))* 
  
You can always add the language you want by sending a Pull Request.  
Many of the translations were chicken-translated, so all except English and Turkish have been removed.  

## License

All code files in this project are licensed under [Apache 2.0](LICENSE)
