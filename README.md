# 🚀 Assistive Android App

An **Assistive Android Application** designed to help users with accessibility features like **voice interaction, emergency SOS system, and profile management**.

---

## 📱 Features

### 🔐 Authentication

* User Signup & Login system
* Session management using SharedPreferences
* Secure user validation using SQLite database

---

### 🧠 Voice Assistant

* 🎤 Speech-to-Text (Voice Input)
* 🔊 Text-to-Speech (Hindi + English support)
* Detects keywords like:

  * "help"
  * "emergency"
  * "bachao"

---

### 🚨 Emergency System

* One-click **Emergency Button**
* Automatic:

  * 📞 Phone Call
  * 📩 SMS with live location
* Google Maps link sent via SMS

---

### 📍 Location Tracking

* Uses **FusedLocationProviderClient**
* Fetches real-time latitude & longitude

---

### 👤 Profile Management

* View user details
* Update:

  * Name
  * Phone number
  * Disability info

---

### 💾 Local Database

* SQLite Database (Offline storage)
* Stores user credentials and profile

---

## 🏗️ Tech Stack

* **Language:** Java
* **IDE:** Android Studio
* **Database:** SQLite
* **APIs Used:**

  * TextToSpeech
  * SpeechRecognizer
  * Fused Location Provider
  * SMS Manager

---

## 📂 Project Structure

```
com.example.assitive
│
├── DBHelper.java          # Database handling
├── LoginActivity.java     # Login screen
├── SignupActivity.java    # Registration screen
├── MainActivity.java      # Core features (Voice + Emergency)
├── ProfileActivity.java   # User profile
│
└── res/
    ├── layout/            # XML UI files
    ├── drawable/          # UI styles
    └── values/            # Colors, themes
```

---

## ⚙️ Permissions Required

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.CALL_PHONE"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.SEND_SMS"/>
```

---

## ▶️ How to Run

1. Clone the repository:

```bash
git clone https://github.com/your-username/assistive-app.git
```

2. Open in **Android Studio**

3. Sync Gradle

4. Run on:

   * Emulator OR
   * Physical Android Device

---

## 🧪 Testing Features

* Try voice input: say *"help"* or *"bachao"*
* Click emergency button
* Register new user
* Update profile

---

## ⚠️ Known Limitations

* Passwords stored in plain text (not secure)
* Emergency number is hardcoded
* No logout feature
* Database resets on version upgrade

---

## 🔮 Future Improvements

* 🔐 Password encryption
* ☁️ Firebase integration (cloud sync)
* 📡 Real-time location tracking
* 🤖 AI-based object detection
* 🎨 Advanced UI/UX (Material Design)
* 🔄 Change emergency number from profile

---
