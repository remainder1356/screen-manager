plugins {
    id("java")
}

group = project.property("group")!!
version = project.property("version")!!

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:${project.property("gdxVersion")}")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${project.property("gdxVersion")}")
    implementation("com.badlogicgames.gdx:gdx-platform:${project.property("gdxVersion")}:natives-desktop")

    if(project.property("enableGraalNative") == "true") {
        implementation("io.github.berstanio:gdx-svmhelper-annotations:${project.property("graalHelperVersion")}")
    }
}

tasks.test {
    useJUnitPlatform()
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