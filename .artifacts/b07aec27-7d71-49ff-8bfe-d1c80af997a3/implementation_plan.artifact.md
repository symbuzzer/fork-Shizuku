# MIUI Optimization for Background Start and Notification Actions

This plan addresses issues reported on MIUI devices where:
1. The "Attempt now" (Şimdi dene) button in the notification does not trigger Shizuku's start.
2. The notification incorrectly reports "Waiting for Wi-Fi connection" even when Wi-Fi is connected after a reboot.

These issues are primarily caused by MIUI's aggressive background management and conservative `WorkManager` constraint handling (especially for `NetworkType.UNMETERED`).

## User Review Required

> [!IMPORTANT]
> The proposed changes relax the network constraint from `UNMETERED` to `CONNECTED` for the background start process. While Shizuku uses local network communication (which doesn't consume internet data), this change ensures the worker runs even if MIUI misclassifies the Wi-Fi connection as metered.

## Proposed Changes

### [Component] Background Worker & Notification Handling

#### [MODIFY] [AdbStartWorker.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/worker/AdbStartWorker.kt)
- Relax the default constraint in `enqueue` from `UNMETERED` to `CONNECTED`.
- Add a `force` parameter to `enqueue` to bypass network constraints and use `setExpedited` for immediate execution when requested by the user.
- Ensure `doWork` calls `setForeground` early to prevent being killed by the system.

#### [MODIFY] [NotifAttemptReceiver.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/receiver/NotifAttemptReceiver.kt)
- Update to call `AdbStartWorker.enqueue(context, force = true)` to ensure the "Attempt now" action is prioritized.

#### [MODIFY] [ShizukuReceiverStarter.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/receiver/ShizukuReceiverStarter.kt)
- Implement a Wi-Fi connectivity check in the `start` method.
- Avoid showing the "Waiting for Wi-Fi" notification if the device is already connected to Wi-Fi.

#### [MODIFY] [EnvironmentUtils.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/utils/EnvironmentUtils.kt)
- Add a helper method `isWifiConnected()` to reliably detect Wi-Fi status across different Android versions.

## Verification Plan

### Automated Tests
- Build and run the project to ensure no regressions in the worker logic.
- Verify `WorkManager` enqueuing behavior via logs.

### Manual Verification
- Deploy to a MIUI device (or emulator).
- Verify that clicking "Attempt now" starts the Shizuku service immediately.
- Reboot the device and verify that the notification accurately reflects the Wi-Fi status.
