# Walkthrough: Removal of Stealth Mode Feature

I have successfully removed the "Hide Shizuku from other apps" (Stealth Mode) feature from the project. This included cleaning up UI components, logic, resources, and dependencies.

## Changes Made

### UI and Navigation
- Removed the Stealth Mode card from the Home screen by modifying [HomeAdapter.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/home/HomeAdapter.kt).
- Deleted [StealthViewHolder.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/home/StealthViewHolder.kt).
- Deleted the entire `stealth` package containing `StealthTutorialActivity.kt` and `StealthTutorialViewModel.kt`.

### Business Logic
- Deleted [ApkUtils.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/utils/ApkUtils.kt) and [ApkSigner.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/utils/ApkSigner.kt), which were exclusively used for cloning and stubbing APKs in Stealth Mode.

### Resources and Manifest
- Modified [AndroidManifest.xml](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/AndroidManifest.xml) to remove the Stealth activity and the `REQUEST_INSTALL_PACKAGES` / `REQUEST_DELETE_PACKAGES` permissions.
- Deleted layout files: `home_stealth.xml` and `stealth_tutorial_activity.xml`.
- Cleaned up string resources in `strings.xml` for English, Turkish, and Portuguese.

### Dependencies and Build Config
- Modified [manager/build.gradle](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/build.gradle) to remove `ARSCLib` and `apksig` libraries.
    > [!NOTE]
    > `BouncyCastle` was retained as it is also used for ADB key generation in `AdbKey.kt`.
- Removed related keep rules from [proguard-rules.pro](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/proguard-rules.pro).

### Other Modules and Documentation
- Updated [ShizukuShellLoader.java](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/shell/src/main/java/rikka/shizuku/shell/ShizukuShellLoader.java) to remove Stealth Mode references from error messages.
- Updated [README.md](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/README.md) to reflect the removal of the feature and its associated permissions.

## Verification Results

### Automated Tests
- Executed `./gradlew :manager:assembleDebug`: **SUCCESS**
- Executed `./gradlew :shell:assembleDebug`: **SUCCESS**

### Manual Verification
- The Home screen no longer displays the "Hide Shizuku from other apps" card.
- All related code and resources have been purged from the project.
