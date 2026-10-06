# Verification evidence

Date: 2026-10-06. Branch: codex/discovery-mode-toolbar. Android only.

- assembleDebug completed successfully.
- testDebugUnitTest completed: 510 tests, zero failures.
- lintDebug executed. The new strings are translated in English, Spanish and Japanese. Lint still fails on CredManMissingDal in the unchanged AndroidManifest.xml. No suppression or unrelated authentication change was added.
- Android CLI inspection on Pixel_10 emulator, Android 17, using Offline Demo: toolbar icon is immediately left of filters; cards have no mode selector above them; the centered dialog has two radio options and no Save button.
- Selected Crystal Capsule: modal closed automatically, toolbar tint changed and the photo became veiled. Reopened dialog: Crystal Capsule was checked. Selected Normal: modal closed and the normal photo returned.
- Horizontal swipe on card content moved from Aoi to Kenji and exposed the existing undo control.
- Screenshots were captured and visually inspected: app/build/discovery-toolbar.png, discovery-mode-dialog.png, discovery-capsule.png and discovery-normal-restored.png. These local build artifacts are intentionally excluded from Git.

Limits: this is a focused emulator Demo check. Physical-device acceptance, TalkBack execution, all screen sizes, dark mode, exhaustive gesture regressions and production network failures were not tested. No performance or FPS claim is made. Existing repositories and ViewModels retain server-confirmed preference and error behavior.
