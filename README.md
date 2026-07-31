# Shizuku-fork

The new and easy-to-use Shizuku variant.

## What is Shizuku?
An Android app that allows other apps to use system-level APIs that require adb/root privileges.

## Screenshots
<img width="108" height="287" alt="main-stopped" src="https://github.com/user-attachments/assets/4438cfe1-7a2e-418f-aa17-dacc9bbb471d" />
<img width="108" height="313" alt="main-running" src="https://github.com/user-attachments/assets/ca3c2420-b81c-4eda-b2a5-01f040ccdf43" />
<img width="108" height="313" alt="settings" src="https://github.com/user-attachments/assets/f0dae81d-000f-4751-ba79-77684ea6ea9e" />

## Disclaimer

This is a **FORK** of [unofficial Shizuku fork by thedjchi](https://github.com/thedjchi/Shizuku).
- If you are looking for the original Shizuku, please visit the [RikkaApps/Shizuku](https://github.com/RikkaApps/Shizuku) repository.
- If you are looking for the Shizuku fork that served as the base for this variant, please visit the [thedjchi/Shizuku](https://github.com/thedjchi/Shizuku) repository.

*Note: This is for my own use. Use it at your own risk.*

## Download

You can download the APK file from IzzyOnDroid FOSS Repo  
Changelogs are distributed via [CHANGELOG.md](CHANGELOG.md) file.  

## How to install
1- **Remove all Shizuku variants before installing and reboot the device.** Because, all of them uses same package names.  
2- **Disable Play Protect before installation.** Because, offical Shizuku is already uploaded to Play Store and it has same package name but different signature. So Play Protect detects all variants of Shizuku as malicious.  
3- **Download and install the [latest apk](https://github.com/symbuzzer/fork-Shizuku/releases/latest/download/Shizuku-fork.apk)**  
4- **Follow on-screen instructions.**  

*Note: During the first setup and each reboot, the device should only be connected to a Wi-Fi network once.*

## Added features by symbuzzer

This variant of thedjchi's Shizuku includes some extra features over the unoffical thedjchi's forked version, such as:
* **Fewer options, much easier to use:** Many options/features have been hidden and will only be shown when needed. Additionally, those I considered redundant have been removed entirely.
* **The initial setup process has been made more automated:** Instead of displaying unnecessary notifications/warnings, processes are now automated. Additionally, the "Pairing" button has been completely removed. All processes can now be completed using only the "Start" button.
* **All external links that I deemed unnecessary have been removed:** The instructions in the application interface are quite adequate.
* **User experience has been improved:** The "Stop" button and "About" section have been moved to more native locations.
* **The "Stealth mode" and "In-app updates" features were removed to allow the application to be published on the IzzyOnDroid Repository.**
* **The language selection feature has been integrated into Android's native feature:** Instead of changing the language from the app's own settings, you can change it in a more integrated way through Android's settings.
* **Turkish translations fixed.**
* **Portuguese translation added.**

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
* **WRITE_SECURE_SETTINGS:** used to toggle USB and wireless debugging in the background when starting/stopping Shizuku

## Translations
Supported languages for now:  
- EN
- TR
- PT *(thanks to [marciozomb13](https://github.com/symbuzzer/fork-Shizuku/issues?q=is%3Apr+author%3Amarciozomb13))* 
  
You can always add the language you want by sending a Pull Request.  
Many of the translations were chicken-translated, so all except English and Turkish have been removed.  

## License

All code files in this project are licensed under [Apache 2.0](LICENSE)
