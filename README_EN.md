# Screen Manager

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![JitPack](https://jitpack.io/v/remainder1356/screen-manager.svg)](https://jitpack.io/#remainder1356/screen-manager)

> A screen management library based on [LibGDX](https://libgdx.com/), providing screen switching, transition animations, hotkey registration, layout splitting, and more.

---

## Features

- **Screen Management** — Supports screen stack for forward/backward navigation.
- **Transition Animations** — Built-in fade and slide screen transitions, with support for custom transitions.
- **Hotkey System** — Supports single-key and combo-key (Ctrl/Alt/Shift) hotkey registration.
- **Split Layout** — `SplitStage` provides a 9-grid layout (top/bottom/left/right/center/corners), auto-adapting to viewport offsets.
- **Priority Rendering** — `PriorityGroup` controls actor draw order by priority (lower values are drawn first, at the bottom layer).
- **Font Utilities** — FreeType-based font loading with dynamic glyph generation, customizable size, and scaling.
- **Auto Logger** — `AutoLogger` interface provides convenient logging methods with automatic class name as tag.
- **Reflection Utilities** — `ReflectUtil` provides convenient reflection field/method read/write tools.
- **Window Attributes** — Supports setting window always-on-top and mouse passthrough.

---

## Requirements

- JDK 21+
- Gradle 8.x
- LibGDX 1.13.5+

---

## Quick Start

### Add Dependency

**Gradle (build.gradle.kts)**

```kotlin
repositories {
    mavenCentral()
    maven { url("https://jitpack.io") }
}

dependencies {
    implementation("com.github.remainder1356:screen-manager:1.0.14")
}
```

### Basic Usage

```java
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.remainder.screen.Screen;
import com.remainder.screen.ScreenManager;
import com.remainder.screen.transition.FadeScreenTransition;

public class MyGame extends ScreenManager {
    @Override
    public void create() {
        super.create();
        setScreen(new MainMenuScreen());
    }
}

class MainMenuScreen extends Screen {
    @Override
    public void show() {
        super.show();
        // Initialize UI elements
        stage.addUIActor(/* ... */);
    }
}
```

---

## Detailed Features

### Screen Switching

```java
// Switch to a new screen (no transition)
ScreenManager.instance.setScreen(new GameScreen());

// Switch to a new screen (with fade transition)
ScreenManager.instance.setScreen(new GameScreen(), new FadeScreenTransition());

// Switch to a new screen (slide in from right)
ScreenManager.instance.setScreen(new GameScreen(),
    new SlideScreenTransition(SlideScreenTransition.FROM_RIGHT));

// Go back to the previous screen (default fade transition)
ScreenManager.instance.toLastScreen();

// Go back to the previous screen (custom transition)
ScreenManager.instance.toLastScreen(new SlideScreenTransition(SlideScreenTransition.FROM_LEFT));
```

### Register Hotkeys

```java
// Single-key hotkey
hotkeyListener.registerHotkey(Input.Keys.ESCAPE, () -> {
    ScreenManager.instance.toLastScreen();
});

// Combo-key hotkey (Ctrl+S)
hotkeyListener.registerComboKey(ComboKey.ctrl(Input.Keys.S), () -> {
    System.out.println("Save");
});

// Hotkey with modifiers
hotkeyListener.registerHotkeyWithCtrl(Input.Keys.Z, () -> {
    System.out.println("Ctrl+Z Undo");
});
hotkeyListener.registerHotkeyWithAlt(Input.Keys.ENTER, () -> {
    System.out.println("Alt+Enter");
});
hotkeyListener.registerHotkeyWithShift(Input.Keys.F1, () -> {
    System.out.println("Shift+F1 Help");
});

// Unregister hotkeys
hotkeyListener.unregisterHotkey(Input.Keys.ESCAPE);
hotkeyListener.unregisterComboKey(ComboKey.ctrl(Input.Keys.S));
```

### SplitStage Layout

`SplitStage` provides a 9-grid split layout, automatically dividing the screen into 9 regions with customizable split ratio and offsets:

```java
SplitStage sStage = new SplitStage(viewport, batch);

// Get region tables
Table center = sStage.getCenter();
center.add("Center Area");

Table top = sStage.getTop();
top.add("Top Area");

Table bottomLeft = sStage.getBottomLeft();
bottomLeft.add("Bottom-Left Corner");

// Set split ratio (default 0.4)
sStage.setSplit(0.3f);

// Set offsets
sStage.setOffTop(10);
sStage.setOffBottom(10);
```

### PriorityGroup Render Order

`PriorityGroup` serves as the root node of `Stage`, controlling render order by priority:

```java
PriorityGroup group = stage.getPriorityGroup();

// Lower priority values are drawn first (at the bottom layer)
group.addActor(backgroundImage, 0);    // Background
group.addActor(uiPanel, 10);           // UI Panel
group.addActor(tooltip, 100);          // Tooltip (topmost layer)

// Convenience methods
stage.addActor(actor, 5);              // Add with specified priority
stage.addUIActor(actor);               // Add with UI priority (0x40000000)
```

### Font Utilities

FreeType-based font wrapper with dynamic glyph generation for characters not pre-loaded:

```java
// Use default font
Font font = DefaultFont.getFont();

// Custom font file
Font customFont = Font.createFont("fonts/NotoSansSC.ttf", Files.FileType.Internal, 24);

// Dynamic glyph generation: automatically generates glyphs for characters
// that are not pre-loaded, suitable for large character sets like Chinese

// Scaling
font.scale(1.5f);
font.scaleSize(1.5f);

// Layout measurement
GlyphLayout layout = font.createGlyphLayout("Hello World");
float textWidth = layout.width;
```

### Window Attributes

```java
// Set window always-on-top
ScreenManager.instance.setFloating(true);

// Set mouse passthrough (window does not intercept mouse events)
ScreenManager.instance.setMousePassThrough(true);
```

### Auto Logger

Implement the `AutoLogger` interface to get convenient logging methods with automatic class name as tag:

```java
public class MyClass implements AutoLogger {
    public void doSomething() {
        log("This is a regular log");
        debug("This is a debug log");
        error("This is an error log");
    }
}
```

---

## Custom Transitions

Extend `ScreenTransition` and implement the `render` method:

```java
public class ZoomScreenTransition extends ScreenTransition {
    public ZoomScreenTransition() {
        super(0.5f, Interpolation.exp5Out);
    }

    @Override
    public void render(TextureRegion last, TextureRegion cur, float delta, Batch batch) {
        super.render(last, cur, delta, batch);
        // progress: transition progress (0.0 ~ 1.0)
        // last: texture of the previous screen
        // cur: texture of the current screen
    }
}
```

---

## Module Overview

| Module | Package | Description |
|--------|---------|-------------|
| `ScreenManager` | `com.remainder.screen` | Manages screen lifecycle and transitions |
| `Screen` | `com.remainder.screen` | Base screen class, extend to implement business logic |
| `ScreenTransition` | `com.remainder.screen.transition` | Abstract base class for transition animations |
| `FadeScreenTransition` | `com.remainder.screen.transition` | Fade-in/fade-out transition |
| `SlideScreenTransition` | `com.remainder.screen.transition` | Slide transition (left/right/top/bottom) |
| `Stage` | `com.remainder.input` | Extended Stage with integrated PriorityGroup and alpha drawing |
| `SplitStage` | `com.remainder.input` | 9-grid split layout Stage |
| `HotkeyListener` | `com.remainder.input` | Hotkey listener |
| `ComboKey` | `com.remainder.input` | Combo-key definition (Java Record) |
| `PriorityGroup` | `com.remainder.util` | Priority-based rendering Group |
| `Font` | `com.remainder.util.font` | FreeType font wrapper with dynamic glyph generation |
| `DefaultFont` | `com.remainder.util.font` | Default font manager |
| `AutoLogger` | `com.remainder.util` | Auto-logging interface |
| `ReflectUtil` | `com.remainder.util` | Reflection utility class |

---

## Build

```bash
# Clone the repository
git clone https://github.com/remainder1356/screen-manager.git
cd screen-manager

# Build the project
./gradlew build

# Run tests
./gradlew test
```

---

## License

[MIT License](LICENSE) © 2026 remainder1356