plugins {
    id("java")
}

group = project.property("group")!!
version = project.property("version")!!

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.badlogicgames.gdx:gdx:${project.property("gdxVersion")}")

    if(project.property("enableGraalNative") == "true") {
        implementation("io.github.berstanio:gdx-svmhelper-annotations:${project.property("graalHelperVersion")}")
    }
}

tasks.test {
    useJUnitPlatform()
}