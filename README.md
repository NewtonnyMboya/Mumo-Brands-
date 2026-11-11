# Mumo Brands Printing and Branding Services

Native Android starter app for Mumo Brands Printing and Branding Services.

## Included
- Navy, magenta, white and royal-blue branded interface
- Material 3 collapsible navigation drawer (outside-tap dismissal)
- Orders with status filters and new-order entry
- Clothing inventory and client records
- Sales summary dashboard
- Room database stored in Android app-private internal storage
- GitHub Actions workflow for debug APK builds

## Build
Requires JDK 17 and Gradle 8.13. Run:

`gradle :app:assembleDebug`

APK output: `app/build/outputs/apk/debug/app-debug.apk`

Or open the **Actions** tab and run **Build Mumo Brands APK**. The resulting
`Mumo-Brands-debug-apk` artifact contains the installable debug APK.

## Development status
This is an initial development build. Printing, editing/deleting, backup/export,
advanced stock movements, authentication and production signing remain future work.
Test on devices before business use.
