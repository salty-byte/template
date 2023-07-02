import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.0"
	id("com.google.protobuf") version "0.8.18"
}

group = "io.saltybyte"
version = "0.0.1-SNAPSHOT"

ext["grpcVersion"] = "1.56.0"
ext["protobufVersion"] = "3.20.1"
ext["grpcKotlinVersion"] = "1.3.0"
ext["springBootVersion"] = "3.1.1"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	// JWT
	implementation("com.auth0:java-jwt:4.4.0")

	// Spring
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
	implementation("org.springframework.boot:spring-boot-starter:${rootProject.ext["springBootVersion"]}")
	implementation("org.springframework.boot:spring-boot-starter-security:${rootProject.ext["springBootVersion"]}")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:${rootProject.ext["springBootVersion"]}")
	testImplementation("org.springframework.boot:spring-boot-starter-test:${rootProject.ext["springBootVersion"]}")

	// gRPC
	implementation("io.github.lognet:grpc-spring-boot-starter:5.1.2")
	implementation("io.grpc:grpc-protobuf:${rootProject.ext["grpcVersion"]}")
	implementation("io.grpc:grpc-services:${rootProject.ext["grpcVersion"]}")
	implementation("io.grpc:grpc-kotlin-stub:${rootProject.ext["grpcKotlinVersion"]}")
	runtimeOnly("io.grpc:grpc-netty:${rootProject.ext["grpcVersion"]}")

	// Test
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks {
	register("fatJar", Jar::class.java) {
		archiveClassifier.set("all")
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
		manifest.attributes["Main-Class"] = "io.saltybyte.MainKt"
		from(configurations.runtimeClasspath.get()
			.map { if (it.isDirectory) it else zipTree(it) })
		val sourcesMain = sourceSets.main.get()
		from(sourcesMain.output)
	}
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:${rootProject.ext["protobufVersion"]}"
	}
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:${rootProject.ext["grpcVersion"]}"
		}
		id("grpckt") {
			artifact =
				"io.grpc:protoc-gen-grpc-kotlin:${rootProject.ext["grpcKotlinVersion"]}:jdk8@jar"
		}
	}
	generateProtoTasks {
		all().forEach {
			it.plugins {
				id("grpc")
				id("grpckt")
			}
		}
	}
}
