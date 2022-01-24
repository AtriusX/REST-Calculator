import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}

group = "xyz.atrius"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	// REST API support
	implementation("org.springframework.boot:spring-boot-starter-web")
	// Arrow installation
	implementation(platform("io.arrow-kt:arrow-stack:1.0.1"))
	implementation("io.arrow-kt:arrow-core")
	// Test framework
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

task<Exec>("dockerBuild") {
	dependsOn(tasks["bootJar"])
	commandLine("docker", "build", "--tag=${project.name}:latest", ".")
}

task<Exec>("dockerRun") {
	dependsOn(tasks["dockerBuild"])
	commandLine("docker", "run", "-p8080:8080", "demo:latest")
}
