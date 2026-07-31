# Remove "Hide Shizuku from other apps" (Stealth Mode) Feature

The "Hide Shizuku from other apps" (also known as Stealth Mode) allows users to clone Shizuku with a different package name or create a stub APK to evade detection by other apps. The user wants this feature and all related code, resources, and dependencies to be completely removed from the project.

## User Review Required

> [!IMPORTANT]
> This change will remove the ability to use Shizuku in "Stealth Mode". If a user is currently using a cloned version of Shizuku, they will need to switch back to the original version as the cloning logic will be removed.

## Proposed Changes

### Manager Module (`:manager`)

The bulk of the changes will occur here as this is where the UI and logic for stealth mode reside.

#### [MODIFY] [HomeAdapter.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/home/HomeAdapter.kt)
- Remove `ID_STEALTH` constant.
- Remove `addItem(StealthViewHolder.CREATOR, null, ID_STEALTH)` call.

#### [DELETE] [StealthViewHolder.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/home/StealthViewHolder.kt)
- This file is used only to display the stealth mode card in the home screen.

#### [DELETE] `stealth` package (file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/stealth/)
- Delete `StealthTutorialActivity.kt` and `StealthTutorialViewModel.kt`.

#### [DELETE] [ApkUtils.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/utils/ApkUtils.kt)
- This utility file handles APK cloning, stub creation, and installation/uninstallation for the stealth feature.

#### [DELETE] [ApkSigner.kt](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/java/moe/shizuku/manager/utils/ApkSigner.kt)
- This file is used by `ApkUtils.kt` to sign the cloned/stub APKs.

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/AndroidManifest.xml)
- Remove `StealthTutorialActivity` declaration.
- Remove `REQUEST_DELETE_PACKAGES` and `REQUEST_INSTALL_PACKAGES` permissions (only used by stealth mode).

#### [DELETE] Layout Resources
- [home_stealth.xml](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/res/layout/home_stealth.xml)
- [stealth_tutorial_activity.xml](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/res/layout/stealth_tutorial_activity.xml)

#### [MODIFY] String Resources
- Remove all `stealth_` and `home_stealth_` prefixed strings from:
    - [strings.xml](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/res/values/strings.xml)
    - [strings.xml (tr)](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/res/values-tr/strings.xml)
    - [strings.xml (pt-rBR)](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/src/main/res/values-pt-rBR/strings.xml)

#### [MODIFY] [build.gradle](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/build.gradle)
- Remove unused dependencies:
    - `io.github.reandroid:ARSCLib:1.3.8`
    - `com.android.tools.build:apksig:8.13.2`
    - `org.bouncycastle:bcpkix-jdk18on:1.80`

#### [MODIFY] [proguard-rules.pro](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/manager/proguard-rules.pro)
- Remove `ARSCLib` related keep rules.

---

### Shell Module (`:shell`)

#### [MODIFY] [ShizukuShellLoader.java](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/shell/src/main/java/rikka/shizuku/shell/ShizukuShellLoader.java)
- Remove stealth mode mention from the timeout warning message.

---

### Documentation

#### [MODIFY] [README.md](file:///C:/Users/Ali Beyaz/AndroidStudioProjects/fork-Shizuku/README.md)
- Remove "Stealth mode" from the features list.
- Remove related permissions from the privacy/permissions section.

## Verification Plan

### Automated Tests
- Run `./gradlew :manager:assembleDebug` to ensure the project still builds correctly without the removed components.
- Run `./gradlew :shell:assembleDebug` to ensure the shell module still builds.

### Manual Verification
- Deploy the app and verify that the "Hide Shizuku from other apps" card is no longer visible on the home screen.
- Verify that there are no broken links or crashes related to the removed feature.
