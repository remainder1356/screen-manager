# Screen Manager

[English](#english) | [中文](#中文)

---

## English

### Overview

Screen Manager is a lightweight and flexible screen management library for libGDX applications. It provides an elegant solution for managing multiple screens with smooth transition effects, making it easy to build complex multiscreen applications.

### Features

- **Simple Screen Management**: Easily switch between screens with a clean API
- **Built-in Transition Effects**: Includes fade and slide transitions out of the box
- **Customizable Transitions**: Create your own transition effects by extending the base class
- **Screen History Navigation**: Built-in support for navigating back to previous screens
- **Auto-dispose Support**: Optional automatic disposal of screens when they're no longer needed
- **FrameBuffer Rendering**: Efficient rendering using FrameBuffers for smooth transitions
- **Debug Mode**: Built-in debug support for UI development

### Installation

#### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.remainder1356:screen-manager:1.0.0")
}
```

#### Maven

```xml
<dependency>
    <groupId>io.github.remainder1356</groupId>
    <artifactId>screen-manager</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Quick Start

#### 1. Create Your ScreenManager

```java
public class MyGame extends ScreenManager {
    @Override
    public void create() {
        super.create();
        
        // Set the initial screen with a fade transition
        setScreen(new MainMenuScreen(), new FadeScreenTransition());
    }
}
```

#### 2. Create Your Screens

```java
public class MainMenuScreen extends Screen {
    @Override
    public void show() {
        super.show();
        // Initialize your UI components here
    }
    
    @Override
    public void render(float delta) {
        super.render(delta);
        // Custom rendering logic
    }
}
```

#### 3. Switch Between Screens

```java
// Switch to a new screen with fade transition
setScreen(new GameScreen(), new FadeScreenTransition());

// Switch to a new screen with slide transition from left
setScreen(new SettingsScreen(), new SlideScreenTransition(SlideScreenTransition.FROM_LEFT));

// Navigate back to the previous screen
toLastScreen();
```

### Available Transitions

#### FadeScreenTransition

A smooth fade effect that blends between screens.

```java
// Default duration (0.5s)
new FadeScreenTransition()

// Custom duration
new FadeScreenTransition(1.0f)

// Custom duration with interpolation
new FadeScreenTransition(0.8f, Interpolation.fade)
```

#### SlideScreenTransition

Slides the screen in from a specified direction.

```java
// Slide from left
new SlideScreenTransition(SlideScreenTransition.FROM_LEFT)

// Slide from right with custom duration
new SlideScreenTransition(0.7f, SlideScreenTransition.FROM_RIGHT)

// Slide from top with custom interpolation
new SlideScreenTransition(Interpolation.pow2, SlideScreenTransition.FROM_TOP)

// Slide from bottom
new SlideScreenTransition(SlideScreenTransition.FROM_BOTTOM)
```

### Creating Custom Transitions

Extend the `ScreenTransition` class to create your own transition effects:

```java
public class MyCustomTransition extends ScreenTransition {
    public MyCustomTransition(float duration) {
        super(duration);
    }
    
    @Override
    public void render(TextureRegion last, TextureRegion cur, float delta, Batch batch) {
        // Your custom transition rendering logic
        float progress = getProgress();
        
        // Render the transition effect
        // ...
    }
}
```

### Advanced Usage

#### Enable Auto-dispose

Automatically dispose screens when they're no longer needed:

```java
setAutoDispose(true);
```

#### Check Screen History

```java
// Check if there's a previous screen
if (hasLastScreen()) {
    toLastScreen();
}

// Get current screen
Screen current = getCurScreen();

// Get last screen
Screen last = getLastScreen();
```

#### Debug Mode

Enable debug rendering for a specific screen:

```java
setScreen(new MyScreen(), new FadeScreenTransition(), true); // true enables debug mode
```

### Project Structure

```
com.remainder.screen
├── Screen.java                    # Base screen class
├── ScreenManager.java             # Core screen manager
└── transition
    ├── ScreenTransition.java      # Base transition class
    ├── FadeScreenTransition.java  # Fade transition effect
    └── SlideScreenTransition.java # Slide transition effect

com.remainder.util
├── AutoLogger.java                # Automatic logging utility
├── PriorityGroup.java             # Priority-based actor grouping
├── ReflectUtil.java               # Reflection utilities
└── Stage.java                     # Enhanced stage implementation
```

### Dependencies

- **libGDX**: 1.13.5
- Java 8 or higher

### License

This project is open-source and available under the MIT License.

### Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

### Author

- **remainder1356**
- Email: 1507464272@qq.com
- GitHub: https://github.com/remainder1356/screen-manager

---

## 中文

### 项目简介

Screen Manager 是一个专为 libGDX 应用设计的轻量级、灵活的屏幕管理库。它提供了优雅的解决方案来管理多个屏幕，支持流畅的过渡效果，让构建复杂的多屏应用变得简单轻松。

### 主要特性

- **简洁的屏幕管理**: 通过清晰的 API 轻松切换屏幕
- **内置过渡效果**: 开箱即用的淡入淡出和滑动过渡效果
- **可自定义过渡**: 通过继承基类创建自己的过渡效果
- **屏幕历史导航**: 内置支持返回上一个屏幕
- **自动释放支持**: 可选的屏幕自动释放功能
- **帧缓冲渲染**: 使用 FrameBuffer 实现高效的过渡渲染
- **调试模式**: 内置 UI 开发调试支持

### 安装

#### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.remainder1356:screen-manager:1.0.0")
}
```

#### Maven

```xml
<dependency>
    <groupId>io.github.remainder1356</groupId>
    <artifactId>screen-manager</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 快速开始

#### 1. 创建 ScreenManager

```java
public class MyGame extends ScreenManager {
    @Override
    public void create() {
        super.create();
        
        // 使用淡入淡出过渡设置初始屏幕
        setScreen(new MainMenuScreen(), new FadeScreenTransition());
    }
}
```

#### 2. 创建屏幕

```java
public class MainMenuScreen extends Screen {
    @Override
    public void show() {
        super.show();
        // 在这里初始化 UI 组件
    }
    
    @Override
    public void render(float delta) {
        super.render(delta);
        // 自定义渲染逻辑
    }
}
```

#### 3. 切换屏幕

```java
// 使用淡入淡出过渡切换到新屏幕
setScreen(new GameScreen(), new FadeScreenTransition());

// 使用从左滑入的过渡切换到新屏幕
setScreen(new SettingsScreen(), new SlideScreenTransition(SlideScreenTransition.FROM_LEFT));

// 返回上一个屏幕
toLastScreen();
```

### 可用的过渡效果

#### FadeScreenTransition（淡入淡出）

在屏幕之间平滑融合的淡入淡出效果。

```java
// 默认持续时间（0.5秒）
new FadeScreenTransition()

// 自定义持续时间
new FadeScreenTransition(1.0f)

// 自定义持续时间和插值器
new FadeScreenTransition(0.8f, Interpolation.fade)
```

#### SlideScreenTransition（滑动）

从指定方向滑入屏幕。

```java
// 从左侧滑入
new SlideScreenTransition(SlideScreenTransition.FROM_LEFT)

// 从右侧滑入，自定义持续时间
new SlideScreenTransition(0.7f, SlideScreenTransition.FROM_RIGHT)

// 从顶部滑入，自定义插值器
new SlideScreenTransition(Interpolation.pow2, SlideScreenTransition.FROM_TOP)

// 从底部滑入
new SlideScreenTransition(SlideScreenTransition.FROM_BOTTOM)
```

### 创建自定义过渡效果

继承 `ScreenTransition` 类来创建自己的过渡效果：

```java
public class MyCustomTransition extends ScreenTransition {
    public MyCustomTransition(float duration) {
        super(duration);
    }
    
    @Override
    public void render(TextureRegion last, TextureRegion cur, float delta, Batch batch) {
        // 自定义过渡渲染逻辑
        float progress = getProgress();
        
        // 渲染过渡效果
        // ...
    }
}
```

### 高级用法

#### 启用自动释放

当屏幕不再需要时自动释放：

```java
setAutoDispose(true);
```

#### 检查屏幕历史

```java
// 检查是否有上一个屏幕
if (hasLastScreen()) {
    toLastScreen();
}

// 获取当前屏幕
Screen current = getCurScreen();

// 获取上一个屏幕
Screen last = getLastScreen();
```

#### 调试模式

为特定屏幕启用调试渲染：

```java
setScreen(new MyScreen(), new FadeScreenTransition(), true); // true 启用调试模式
```

### 项目结构

```
com.remainder.screen
├── Screen.java                    # 基础屏幕类
├── ScreenManager.java             # 核心屏幕管理器
└── transition
    ├── ScreenTransition.java      # 基础过渡类
    ├── FadeScreenTransition.java  # 淡入淡出过渡效果
    └── SlideScreenTransition.java # 滑动过渡效果

com.remainder.util
├── AutoLogger.java                # 自动日志工具
├── PriorityGroup.java             # 基于优先级的 Actor 分组
├── ReflectUtil.java               # 反射工具
└── Stage.java                     # 增强的 Stage 实现
```

### 依赖项

- **libGDX**: 1.13.5
- Java 8 或更高版本

### 开源协议

本项目是开源的，遵循 MIT 许可证。

### 贡献

欢迎贡献！请随时提交问题和拉取请求。

### 作者

- **remainder1356**
- 邮箱: 1507464272@qq.com
- GitHub: https://github.com/remainder1356/screen-manager
