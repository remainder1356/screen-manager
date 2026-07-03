# Screen Manager

[![](https://jitpack.io/v/remainder1356/screen-manager.svg)](https://jitpack.io/#remainder1356/screen-manager)

一个基于 [LibGDX](https://libgdx.com/) 的屏幕管理与切换库，提供灵活的屏幕生命周期管理、热键监听、屏幕过渡动画等功能。

**快速跳转**: [特性](#特性) | [安装](#安装) | [快速开始](#快速开始) | [核心组件](#核心组件) | [API示例](#api示例)

---

## 特性

- **屏幕管理** - 简单的屏幕切换与历史记录管理
- **过渡动画** - 支持淡入淡出、滑动等多种过渡效果
- **热键系统** - 便捷的键盘快捷键注册，支持组合键
- **分屏布局** - 内置9宫格分屏Stage，快速构建复杂UI
- **优先级渲染** - Actor层级自定义，UI渲染顺序可控
- **自动释放** - 可配置自动释放旧屏幕资源

---

## 安装

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

## 快速开始

### 1. 创建 Application

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

### 2. 创建 Screen

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

### 3. 切换屏幕

```java
// 普通切换
setScreen(new GameScreen());

// 带过渡动画
setScreen(new GameScreen(), new FadeScreenTransition());

// 返回上一屏幕
toLastScreen();
```

---

## 核心组件

### ScreenManager

屏幕管理器，负责所有屏幕的生命周期。

| 方法 | 说明 |
|------|------|
| `setScreen(Screen)` | 切换到指定屏幕 |
| `setScreen(Screen, ScreenTransition)` | 带过渡动画切换 |
| `toLastScreen()` | 返回上一屏幕 |
| `toLastScreen(ScreenTransition)` | 带动画返回上一屏幕 |
| `getCurScreen()` | 获取当前屏幕 |
| `hasLastScreen()` | 是否有上一屏幕 |
| `setAutoDispose(boolean)` | 设置是否自动释放旧屏幕 |

### Screen

屏幕基类，继承它来创建你的游戏屏幕。

```java
public class MyScreen extends Screen {
    @Override
    public void show() {
        // 屏幕显示时调用
    }

    @Override
    public void renderOther(float delta) {
        // 自定义渲染（stage.draw()之外）
    }
}
```

### HotkeyListener

热键监听器，处理键盘输入。

```java
// ESC键
hotkeyListener.registerHotkey(Keys.ESCAPE, () -> doSomething());

// Ctrl+S
hotkeyListener.registerHotkeyWithCtrl(Keys.S, () -> save());

// 推荐：使用ComboKey处理组合键
hotkeyListener.registerComboKey(ComboKey.ctrl(Keys.S), () -> save());
hotkeyListener.registerComboKey(ComboKey.ctrlShift(Keys.DELETE), () -> hardReset());
```

### SplitStage

分屏布局，适合游戏设置界面或聊天UI。

```java
SplitStage splitStage = new SplitStage(stage.getViewport(), stage.getBatch());
splitStage.setSkin(VisUI.getSkin());

// 添加内容到各个区域
splitStage.getLeft().add("Left Panel");
splitStage.getCenter().add("Center Panel");
splitStage.getTop().add("Top Bar");
```

布局结构：
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

## API示例

### 屏幕切换与过渡

```java
// 淡入淡出（默认0.5秒）
setScreen(newScreen, new FadeScreenTransition());

// 淡入淡出，自定义时长
setScreen(newScreen, new FadeScreenTransition(1.0f));

// 滑动过渡（从左滑入）
setScreen(newScreen, new SlideScreenTransition(SlideScreenTransition.FROM_LEFT));

// 滑动过渡，四个方向
SlideScreenTransition.FROM_LEFT   // 从左
SlideScreenTransition.FROM_RIGHT  // 从右
SlideScreenTransition.FROM_TOP    // 从上
SlideScreenTransition.FROM_BOTTOM // 从下
```

### 热键注册

```java
import com.badlogic.gdx.Input.Keys;

// 单键
hotkeyListener.registerHotkey(Keys.ESCAPE, this::onEscape);
hotkeyListener.registerHotkey(Keys.ENTER, this::onEnter);

// 带修饰符
hotkeyListener.registerHotkeyWithCtrl(Keys.S, this::save);
hotkeyListener.registerHotkeyWithAlt(Keys.F4, this::quit);
hotkeyListener.registerHotkeyWithShift(Keys.TAB, this::prevTab);

// 组合键（推荐方式）
hotkeyListener.registerComboKey(ComboKey.ctrl(Keys.N), this::newFile);
hotkeyListener.registerComboKey(ComboKey.ctrlAlt(Keys.DEL), this::forceQuit);
hotkeyListener.registerComboKey(ComboKey.all(Keys.R), this::hardReset);

// 注销热键
hotkeyListener.unregisterHotkey(Keys.ESCAPE);
hotkeyListener.unregisterHotkey(Keys.S, saveCallback);
```

### ComboKey 组合键工厂

```java
ComboKey.ctrl(Keys.S)          // Ctrl+S
ComboKey.alt(Keys.F4)          // Alt+F4
ComboKey.shift(Keys.TAB)       // Shift+Tab
ComboKey.ctrlAlt(Keys.DEL)     // Ctrl+Alt+Delete
ComboKey.shiftCtrl(Keys.R)     // Shift+Ctrl+R
ComboKey.shiftAlt(Keys.END)    // Shift+Alt+End
ComboKey.all(Keys.R)           // Ctrl+Alt+Shift+R
```

### 窗口控制

```java
// 窗口悬浮
setFloating(true);

// 鼠标穿透（点击穿透到下层窗口）
setMousePassThrough(true);
```

---

## 项目结构

```
src/main/java/com/remainder/
├── screen/
│   ├── ScreenManager.java       # 屏幕管理器
│   ├── Screen.java              # 屏幕基类
│   └── transition/              # 过渡动画
│       ├── ScreenTransition.java
│       ├── FadeScreenTransition.java
│       └── SlideScreenTransition.java
├── input/
│   ├── Stage.java              # 扩展Stage
│   ├── SplitStage.java         # 分屏Stage
│   ├── HotkeyListener.java     # 热键监听
│   └── ComboKey.java           # 组合键
└── util/
    ├── AutoLogger.java         # 日志接口
    ├── PriorityGroup.java      # 优先级容器
    ├── ReflectUtil.java        # 反射工具
    └── font/                   # 字体支持
        ├── Font.java
        └── DefaultFont.java
```

---

## 构建

```bash
# 编译
./gradlew compileJava

# 测试
./gradlew test

# 构建JAR
./gradlew jar
```

---

## 依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| LibGDX | 1.13.5 | 核心框架 |
| libGDX Backend LWJGL3 | 1.13.5 | 桌面支持 |
| FreeType | 1.13.5 | 字体渲染 |

---

## License

MIT License
