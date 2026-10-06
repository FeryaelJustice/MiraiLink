# Implementation plan

1. Create a separate Android branch from current master.
2. Add a theme-tinted vector combining switching arrows and a crystal.
3. Add a centered Material dialog to the existing Discovery toolbar action area, before filters.
4. Reuse CapsuleModeViewModel and confirmed preferences. HomeViewModel already observes preference changes and refreshes the feed.
5. Remove the inline selector and its extra layout container from HomeScreen. Keep its capsule mode observation for empty-state guidance.
6. Build, run existing unit tests and lint, and inspect the UI on an available Android device. Record actual evidence and any limitations separately.
7. Commit, push and create a new PR against master. Backend deployment is unnecessary for this visual change.
