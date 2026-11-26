plugins {
    id("java")
}

group = project.property("group")!!
version = project.property("version")!!

repositories {
    mavenCentral()
}

dependencies {
    var gdxVer = project.property("gdxVersion")

    implementation("com.badlogicgames.gdx:gdx:$gdxVer")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVer")
    implementation("com.badlogicgames.gdx:gdx-freetype:$gdxVer")

    testImplementation("com.kotcrab.vis:vis-ui:${project.property("visUiVersion")}")
    testImplementation("com.badlogicgames.gdx:gdx-platform:$gdxVer:natives-desktop")
    testImplementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVer:natives-desktop")
    testImplementation("com.badlogicgames.gdx:gdx-tools:$gdxVer") {
        exclude("com.badlogicgames.gdx", "gdx-backend-lwjgl")
    }

    if(project.property("enableGraalNative") == "true") {
        var graalHelperVersion = project.property("graalHelperVersion")
        implementation("io.github.berstanio:gdx-svmhelper-annotations:${graalHelperVersion}")
        testImplementation("io.github.berstanio:gdx-svmhelper-backend-lwjgl3:${graalHelperVersion}")
        testImplementation("io.github.berstanio:gdx-svmhelper-extension-freetype:${graalHelperVersion}")
    }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.3")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// 创建源代码 JAR
tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

// 创建 Javadoc JAR
tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}