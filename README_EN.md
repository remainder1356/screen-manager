# Screen Manager

A screen management and transition library built on top of [LibGDX](https://libgdx.com/), providing flexible screen lifecycle management, hotkey listening, screen transition animations, and more.

**Quick Links**: [Features](#features) | [Installation](#installation) | [Quick Start](#quick-start) | [Core Components](#core-components) | [API Examples](#api-examples)

---

## Features

- **Screen Management** - Simple screen switching with history tracking
- **Transition Animations** - Support for fade, slide, and other transition effects
- **Hotkey System** - Convenient keyboard shortcut registration with combo key support
- **Split Layout** - Built-in 9-region split Stage for complex UI layouts
- **Priority Rendering** - Customizable Actor layers for controlled UI rendering order
- **Auto Disposal** - Optional automatic resource cleanup for old screens

---

## Installation

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.remainder1356:screen-manager:1.0.11")
}
```

### Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.remainder1356</groupId>
    <artifactId>screen-manager</artifactId>
    <version>1.0.11</version>
</dependency>
```

---

## Quick Start

### 1. Create Your Application

```java
import com.badlogic.gdx.backends.lwjgl3.*;

public class MyApp extends ScreenManager {
    @Override
    public void create() {
        super.create();
        setScreen(new MainMenuScreen());
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("My Game");
        config.setWindowedMode(1280, 720);
        new Lwjgl3Application(new MyApp(), config);
    }
}
```

### 2. Create a Screen

```java
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.remainder.screen.Screen;

public class MainMenuScreen extends Screen {
    @Override
    public void show() {
        stage.addUIActor(new Label("Welcome!", new Label.LabelStyle()));
    }
}
```

### 3. Switch Screens

```java
// Normal switch
setScreen(new GameScreen());

// With transition animation
setScreen(new GameScreen(), new FadeScreenTransition());

// Go back to previous screen
toLastScreen();
```

---

## Core Components

### ScreenManager

The screen manager, responsible for all screen lifecycles.

| Method | Description |
|--------|-------------|
| `setScreen(Screen)` | Switch to the specified screen |
| `setScreen(Screen, ScreenTransition)` | Switch with transition animation |
| `toLastScreen()` | Go back to the previous screen |
| `toLastScreen(ScreenTransition)` | Go back with animation |
| `getCurScreen()` | Get the current screen |
| `hasLastScreen()` | Check if there's a previous screen |
| `setAutoDispose(boolean)` | Set auto disposal of old screens |

### Screen

Base screen class. Extend it to create your game screens.

```java
public class MyScreen extends Screen {
    @Override
    public void show() {
        // Called when screen is shown
    }

    @Override
    public void renderOther(float delta) {
        // Custom rendering (outside of stage.draw())
    }
}
```

### HotkeyListener

Hotkey listener for handling keyboard input.

```java
// Single key
hotkeyListener.registerHotkey(Keys.ESCAPE, () -> doSomething());

// With Ctrl modifier
hotkeyListener.registerHotkeyWithCtrl(Keys.S, () -> save());

// Recommended: Use ComboKey for combo keys
hotkeyListener.registerComboKey(ComboKey.ctrl(Keys.S), () -> save());
hotkeyListener.registerComboKey(ComboKey.ctrlShift(Keys.DELETE), () -> hardReset());
```

### SplitStage

Split layout, suitable for game settings UI or chat interfaces.

```java
SplitStage splitStage = new SplitStage(stage.getViewport(), stage.getBatch());
splitStage.setSkin(VisUI.getSkin());

// Add content to regions
splitStage.getLeft().add("Left Panel");
splitStage.getCenter().add("Center Panel");
splitStage.getTop().add("Top Bar");
```

Layout structure:
```
┌──────────┬──────────┬──────────┐
│ topLeft  │   top    │ topRight │
├──────────┼──────────┼──────────┤
│   left   │  center │  right   │
├──────────┼──────────┼──────────┤
│bottomLeft│  bottom │bottomRight│
└──────────┴──────────┴──────────┘
```

---

## API Examples

### Screen Transitions

```java
// Fade (default 0.5 seconds)
setScreen(newScreen, new FadeScreenTransition());

// Fade with custom duration
setScreen(newScreen, new FadeScreenTransition(1.0f));

// Slide from left
setScreen(newScreen, new SlideScreenTransition(SlideScreenTransition.FROM_LEFT));

// Slide from any direction
SlideScreenTransition.FROM_LEFT   // From left
SlideScreenTransition.FROM_RIGHT  // From right
SlideScreenTransition.FROM_TOP   // From top
SlideScreenTransition.FROM_BOTTOM // From bottom
```

### Hotkey Registration

```java
import com.badlogic.gdx.Input.Keys;

// Single keys
hotkeyListener.registerHotkey(Keys.ESCAPE, this::onEscape);
hotkeyListener.registerHotkey(Keys.ENTER, this::onEnter);

// With modifiers
hotkeyListener.registerHotkeyWithCtrl(Keys.S, this::save);
hotkeyListener.registerHotkeyWithAlt(Keys.F4, this::quit);
hotkeyListener.registerHotkeyWithShift(Keys.TAB, this::prevTab);

// Combo keys (recommended)
hotkeyListener.registerComboKey(ComboKey.ctrl(Keys.N), this::newFile);
hotkeyListener.registerComboKey(ComboKey.ctrlAlt(Keys.DEL), this::forceQuit);
hotkeyListener.registerComboKey(ComboKey.all(Keys.R), this::hardReset);

// Unregister hotkeys
hotkeyListener.unregisterHotkey(Keys.ESCAPE);
hotkeyListener.unregisterHotkey(Keys.S, saveCallback);
```

### ComboKey Factories

```java
ComboKey.ctrl(Keys.S)          // Ctrl+S
ComboKey.alt(Keys.F4)          // Alt+F4
ComboKey.shift(Keys.TAB)       // Shift+Tab
ComboKey.ctrlAlt(Keys.DEL)     // Ctrl+Alt+Delete
ComboKey.shiftCtrl(Keys.R)     // Shift+Ctrl+R
ComboKey.shiftAlt(Keys.END)    // Shift+Alt+End
ComboKey.all(Keys.R)           // Ctrl+Alt+Shift+R
```

### Window Control

```java
// Make window floating
setFloating(true);

// Enable mouse passthrough (clicks pass through to underlying windows)
setMousePassThrough(true);
```

---

## Project Structure

```
src/main/java/com/remainder/
├── screen/
│   ├── ScreenManager.java       # Screen manager
│   ├── Screen.java             # Screen base class
│   └── transition/             # Transition animations
│       ├── ScreenTransition.java
│       ├── FadeScreenTransition.java
│       └── SlideScreenTransition.java
├── input/
│   ├── Stage.java              # Extended Stage
│   ├── SplitStage.java         # Split Stage
│   ├── HotkeyListener.java     # Hotkey listener
│   └── ComboKey.java          # Combo key record
└── util/
    ├── AutoLogger.java         # Logging interface
    ├── PriorityGroup.java      # Priority container
    ├── ReflectUtil.java        # Reflection utility
    └── font/                  # Font support
        ├── Font.java
        └── DefaultFont.java
```

---

## Build

```bash
# Compile
./gradlew compileJava

# Test
./gradlew test

# Build JAR
./gradlew jar
```

---

## Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| LibGDX | 1.13.5 | Core framework |
| libGDX Backend LWJGL3 | 1.13.5 | Desktop support |
| FreeType | 1.13.5 | Font rendering |

---

## License

MIT License
