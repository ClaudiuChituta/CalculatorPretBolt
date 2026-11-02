# Bolt Driver Rate Calculator (Android)

A lightweight Android utility designed for ride-sharing drivers to evaluate ride profitability in real-time. The application dynamically inspects on-screen fare details and calculates the revenue per kilometer ratio using a floating non-intrusive HUD overlay.

---

## Key Features

- **Automated Text Extraction:** Uses Android's `AccessibilityService` to inspect the UI tree and extract price and distance figures in real-time.
- **Dynamic HUD Overlay:** Renders a floating, draggable widget via `WindowManager` (`TYPE_APPLICATION_OVERLAY`) that stays visible over active navigation apps.
- **Visual Feedback:** Color-coded profitability indicators:
  - **Green:** $\ge 3.50$ RON/km (High profitability)
  - **Yellow/Orange:** $2.50 - 3.49$ RON/km (Average return)
  - **Red:** $< 2.50$ RON/km (Low return / high wear)
- **Local-Only Processing:** Zero background tracking; all data parsing and math calculations run on-device.

---

## Technical Stack & Architecture

- **Language:** Kotlin
- **UI:** Jetpack Compose (Settings & Config) + Custom Native View Overlay (`WindowManager`)
- **System APIs:** 
  - `AccessibilityService` (`AccessibilityNodeInfo` tree traversal)
  - `WindowManager` & `SYSTEM_ALERT_WINDOW`
- **Pattern:** Event-driven background service communication

---

## Permissions Handled

- `SYSTEM_ALERT_WINDOW` – Draw the floating HUD over other applications.
- `BIND_ACCESSIBILITY_SERVICE` – Read active window content for fare calculations.
