# Screen Manager

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![JitPack](https://jitpack.io/v/remainder1356/screen-manager.svg)](https://jitpack.io/#remainder1356/screen-manager)

> 基于 [LibGDX](https://libgdx.com/) 的屏幕管理库，提供屏幕切换、过渡动画、热键注册、布局分割等功能。

---

## 特性

- **屏幕管理** — 支持屏幕栈（Screen Stack），可前进/后退切换屏幕。
- **过渡动画** — 内置淡入淡出（Fade）和滑动（Slide）屏幕过渡效果，支持自定义过渡动画。
- **热键系统** — 支持单键热键和组合键（Ctrl/Alt/Shift）热键注册。
- **分割布局** — `SplitStage` 提供九宫格布局（上/下/左/右/中/四角），自动适配视口偏移。
- **优先级渲染** — `PriorityGroup` 支持按优先级控制 Actor 的绘制顺序，数值越小越先绘制（位于底层）。
- **字体工具** — 基于 FreeType 的字体加载封装，支持动态字形生成、自定义字号和缩放。
- **自动日志** — `AutoLogger` 接口提供便捷的日志记录方法，自动使用类名作为日志标签。
- **反射工具** — `ReflectUtil` 提供便捷的反射字段/方法读写工具。
- **窗口属性** — 支持设置窗口置顶（Always-on-Top）和鼠标穿透（Mouse Passthrough）。

---

## 环境要求

- JDK 21+
- Gradle 8.x
- LibGDX 1.13.5+

---

## 快速开始

### 添加依赖

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

### 基本用法

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
        // 初始化 UI 元素
        stage.addUIActor(/* ... */);
    }
}
```

---

## 详细功能

### 屏幕切换

```java
// 切换到新屏幕（无过渡动画）
ScreenManager.instance.setScreen(new GameScreen());

// 切换到新屏幕（带淡入淡出过渡）
ScreenManager.instance.setScreen(new GameScreen(), new FadeScreenTransition());

// 切换到新屏幕（带滑动过渡，从右侧滑入）
ScreenManager.instance.setScreen(new GameScreen(),
    new SlideScreenTransition(SlideScreenTransition.FROM_RIGHT));

// 返回上一屏幕（默认淡入淡出过渡）
ScreenManager.instance.toLastScreen();

// 返回上一屏幕（自定义过渡）
ScreenManager.instance.toLastScreen(new SlideScreenTransition(SlideScreenTransition.FROM_LEFT));
```

### 注册热键

```java
// 单键热键
hotkeyListener.registerHotkey(Input.Keys.ESCAPE, () -> {
    ScreenManager.instance.toLastScreen();
});

// 组合键热键（Ctrl+S）
hotkeyListener.registerComboKey(ComboKey.ctrl(Input.Keys.S), () -> {
    System.out.println("保存");
});

// 带修饰键的热键
hotkeyListener.registerHotkeyWithCtrl(Input.Keys.Z, () -> {
    System.out.println("Ctrl+Z 撤销");
});
hotkeyListener.registerHotkeyWithAlt(Input.Keys.ENTER, () -> {
    System.out.println("Alt+Enter");
});
hotkeyListener.registerHotkeyWithShift(Input.Keys.F1, () -> {
    System.out.println("Shift+F1 帮助");
});

// 取消注册热键
hotkeyListener.unregisterHotkey(Input.Keys.ESCAPE);
hotkeyListener.unregisterComboKey(ComboKey.ctrl(Input.Keys.S));
```

### SplitStage 分割布局

`SplitStage` 提供九宫格分割布局，自动将屏幕划分为 9 个区域，支持自定义分割比例和偏移：

```java
SplitStage sStage = new SplitStage(viewport, batch);

// 获取各区域 Table
Table center = sStage.getCenter();
center.add("中央区域");

Table top = sStage.getTop();
top.add("顶部区域");

Table bottomLeft = sStage.getBottomLeft();
bottomLeft.add("左下角");

// 设置分割比例（默认 0.4）
sStage.setSplit(0.3f);

// 设置偏移量
sStage.setOffTop(10);
sStage.setOffBottom(10);
```

### PriorityGroup 渲染顺序

`PriorityGroup` 是 `Stage` 的根节点，按优先级控制渲染顺序：

```java
PriorityGroup group = stage.getPriorityGroup();

// 优先级数值越小，越先绘制（在底层）
group.addActor(backgroundImage, 0);    // 背景
group.addActor(uiPanel, 10);           // UI 面板
group.addActor(tooltip, 100);          // 提示框（最上层）

// 使用便捷方法添加
stage.addActor(actor, 5);              // 指定优先级
stage.addUIActor(actor);               // 以 UI 优先级添加（0x40000000）
```

### 字体工具

基于 FreeType 的字体封装，支持动态生成未预加载的字形：

```java
// 使用默认字体
Font font = DefaultFont.getFont();

// 自定义字体文件
Font customFont = Font.createFont("fonts/NotoSansSC.ttf", Files.FileType.Internal, 24);

// 动态字形生成：对于未预加载的字符，自动生成字形并添加到字体中
// 适合中文等大字符集场景

// 缩放
font.scale(1.5f);
font.scaleSize(1.5f);

// 创建布局测量
GlyphLayout layout = font.createGlyphLayout("Hello World");
float textWidth = layout.width;
```

### 窗口属性

```java
// 设置窗口置顶
ScreenManager.instance.setFloating(true);

// 设置鼠标穿透（窗口不拦截鼠标事件）
ScreenManager.instance.setMousePassThrough(true);
```

### 自动日志

实现 `AutoLogger` 接口即可获得便捷的日志方法，自动使用类名作为日志标签：

```java
public class MyClass implements AutoLogger {
    public void doSomething() {
        log("这是一条普通日志");
        debug("这是一条调试日志");
        error("这是一条错误日志");
    }
}
```

---

## 自定义过渡动画

继承 `ScreenTransition` 并实现 `render` 方法：

```java
public class ZoomScreenTransition extends ScreenTransition {
    public ZoomScreenTransition() {
        super(0.5f, Interpolation.exp5Out);
    }

    @Override
    public void render(TextureRegion last, TextureRegion cur, float delta, Batch batch) {
        super.render(last, cur, delta, batch);
        // progress: 过渡进度 (0.0 ~ 1.0)
        // last: 上一屏幕的纹理
        // cur: 当前屏幕的纹理
    }
}
```

---

## 模块说明

| 模块 | 包路径 | 说明 |
|------|--------|------|
| `ScreenManager` | `com.remainder.screen` | 屏幕管理器，负责屏幕生命周期和过渡 |
| `Screen` | `com.remainder.screen` | 屏幕基类，需继承并实现业务逻辑 |
| `ScreenTransition` | `com.remainder.screen.transition` | 过渡动画抽象基类 |
| `FadeScreenTransition` | `com.remainder.screen.transition` | 淡入淡出过渡 |
| `SlideScreenTransition` | `com.remainder.screen.transition` | 滑动过渡（左/右/上/下） |
| `Stage` | `com.remainder.input` | 扩展的 Stage，集成 PriorityGroup 和透明度绘制 |
| `SplitStage` | `com.remainder.input` | 九宫格分割布局 Stage |
| `HotkeyListener` | `com.remainder.input` | 热键监听器 |
| `ComboKey` | `com.remainder.input` | 组合键定义（Java Record） |
| `PriorityGroup` | `com.remainder.util` | 优先级渲染 Group |
| `Font` | `com.remainder.util.font` | FreeType 字体封装，支持动态字形生成 |
| `DefaultFont` | `com.remainder.util.font` | 默认字体管理器 |
| `AutoLogger` | `com.remainder.util` | 自动日志接口 |
| `ReflectUtil` | `com.remainder.util` | 反射工具类 |

---

## 构建

```bash
# 克隆仓库
git clone https://github.com/remainder1356/screen-manager.git
cd screen-manager

# 构建项目
./gradlew build

# 运行测试
./gradlew test
```

---

## 许可证

[MIT License](LICENSE) © 2026 remainder1356