# SmokeFree+ 🚭
A mobile health (mHealth) application designed to help users quit smoking by tracking habits, logging cravings, and celebrating smoke-free streaks. Built with **Android Studio (Java/Kotlin)**, the app focuses on ease of use, motivation, and offline availability.

---

## ✨ Features
- **Daily Smoking Log** – Record whether you smoked today (Yes/No).
- **Craving & Trigger Journal** – Note cravings and situations that trigger them.
- **Streak Tracker** – Track consecutive smoke-free days with automatic resets if a relapse occurs.
- **Milestone Badges** – Unlock achievements at 1, 3, 7, 14, and 30 days smoke-free.
- **Progress Dashboard** – View stats on days smoke-free, cigarettes avoided, and money saved.
- **Charts & Visualizations** – Visualize progress using [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart).
- **Offline-First Persistence** – All data stored locally using `SharedPreferences`.
- **Future Work** – Notifications, reminders, and cloud backup.

---

## 📱 Screenshots 
---

## 🛠️ Tech Stack
- **Languages:** Java, Kotlin  
- **IDE:** Android Studio  
- **Libraries:** MPAndroidChart, SharedPreferences, Material UI components  
- **Architecture:** Modular, Offline-First  

---

## 📂 Project Structure
SmokeFreePlus/
├─ app/src/main/java/com/smokefreeplus/
│ ├─ activities/ (MainActivity, DashboardActivity, LogActivity, etc.)
│ ├─ utils/ (SharedPrefManager, BadgeUtils, ChartUtils)
│ └─ models/ (UserLog, Badge, Stats)
├─ app/src/main/res/
│ ├─ layout/ (XML UI layouts)
│ ├─ drawable/ (icons, graphics)
│ └─ values/ (colors.xml, strings.xml, styles.xml)
└─ build.gradle

yaml
Copy code

---

## 🚀 Getting Started

### Prerequisites
- Install [Android Studio](https://developer.android.com/studio)  
- JDK 11+  
- Android SDK

### Setup
```bash
# Clone the repo
git clone https://github.com/Tahniat-Niger/SmokeFreePlus.git
cd SmokeFreePlus

# Open in Android Studio
# Sync Gradle and build the project
Run
Open the project in Android Studio.

Connect an Android device (or use an emulator).

Click Run ▶ to launch the app.
