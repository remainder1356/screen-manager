plugins {
    id("java")
    id("maven-publish")
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name = "Screen Manager"
                description = "A simple screen manager for libGDX"
                url = "https://github.com/remainder1356/screen-manager"

                developers {
                    developer {
                        id = "remainder1356"
                        name = "remainder1356"
                        email = "1507464272@qq.com"
                    }
                }

                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/remainder1356/screen-manager.git"
                    developerConnection = "scm:git:ssh://github.com/remainder1356/screen-manager.git"
                    url = "https://github.com/remainder1356/screen-manager"
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("file://E:/maven/repo")
        }
    }
}