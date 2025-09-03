plugins {
    idea
    `java-library`
    `maven-publish`
    jacoco
}

group = "fr.rakambda"
description = "Console progress bars library"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(libs.log4j2Bom))
    
    api(libs.slf4j)
    implementation(libs.bundles.log4j2)

    api(libs.jline)
    api(libs.jSpecify)

    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)

    implementation(platform(libs.junitBom))
    testImplementation(libs.bundles.junit)
    testRuntimeOnly(libs.junitPlatformLauncher)

    testImplementation(libs.bundles.assertj)
    testImplementation(libs.bundles.mockito)

    testCompileOnly(libs.lombok)

    testAnnotationProcessor(libs.lombok)
}

tasks {
    processResources {
        expand(project.properties)
    }

    compileJava {
        val moduleName: String by project
        inputs.property("moduleName", moduleName)

        options.encoding = "UTF-8"
        options.isDeprecation = true
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }

    jar {
        manifest {
            attributes["Multi-Release"] = "true"
        }
    }

    test {
        useJUnitPlatform()
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
        }
    }
}

tasks.register<Test>("demo") {
    useJUnitPlatform()
    group = "demo"
    filter {
        includeTestsMatching("fr.rakambda.progressbar.ShowcaseDemo.run")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

jacoco {
    toolVersion = libs.versions.jacocoVersion.get()
}

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "fr.rakambda"
            artifactId = "progressbar"

            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/RakambdaOrg/ProgressBars")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}