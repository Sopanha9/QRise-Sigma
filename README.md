# QRise-Sigma 🔔

An Android alarm app where the alarm **only stops when you scan the correct QR code**.

## Features
- Set alarms with a time picker
- Auto-generates a unique QR code per alarm
- Alarm rings with sound + vibration
- **Can only be dismissed by scanning the exact QR code** — no snooze button escape
- Firebase Auth (login/register)
- Firebase Firestore sync (alarms backed up to cloud)
- Local Room DB (works offline)
- Reschedules alarms after device reboot

## Tech Stack
- Kotlin
- Firebase Auth + Firestore
- Room (local DB)
- ZXing (QR generation + scanning)
- MVVM + LiveData
- Android AlarmManager (exact alarms)
- Foreground Service (alarm ringing)

## Setup

1. Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
2. Add an Android app with package name `com.sopanha.qrisesigma`
3. Download `google-services.json` and replace `app/google-services.json`
4. Enable **Email/Password** auth in Firebase Console
5. Enable **Firestore** in Firebase Console
6. Open in Android Studio and run

## How It Works
1. Set an alarm → app generates a unique QR code
2. Save/print the QR code
3. When the alarm fires → phone rings and vibrates
4. Only way to stop it: open app and scan the exact QR code
5. Wrong QR = alarm keeps ringing
