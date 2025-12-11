// === ВЕРСІЇ БІБЛІОТЕК ===
val javaVersion = JavaVersion.VERSION_21
val javafxVersion = "21.0.1"
val postgresVersion = "42.7.2"
val atlantaFxVersion = "2.0.1"
val controlsFxVersion = "11.2.0"
val ikonliVersion = "12.3.1"
val junitVersion = "5.10.0"

val myJvmArgs = listOf(
    "--enable-native-access=javafx.graphics",
    "--add-opens", "javafx.graphics/javafx.scene=ALL-UNNAMED",
    "--add-opens", "javafx.controls/javafx.scene.control=ALL-UNNAMED",
    "--add-opens", "javafx.base/com.sun.javafx.runtime=ALL-UNNAMED"
)

plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "io.github.serhii0659.JavaLabs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Налаштування Java
java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:-module")
}

// Налаштування JavaFX плагіна
javafx {
    version = javafxVersion
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    // === База даних ===
    implementation("org.postgresql:postgresql:$postgresVersion")

    // === UI та Стилі ===
    implementation("io.github.mkpaz:atlantafx-base:$atlantaFxVersion") // Гарна тема
    implementation("org.controlsfx:controlsfx:$controlsFxVersion")      // Додаткові віджети

    // === Іконки ===
    implementation("org.kordamp.ikonli:ikonli-javafx:$ikonliVersion")
    implementation("org.kordamp.ikonli:ikonli-fontawesome5-pack:$ikonliVersion")

    // === Тести ===
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainModule.set("io.github.serhii0659.JavaLabs")
    mainClass.set("io.github.serhii0659.JavaLabs.Launcher")

    applicationDefaultJvmArgs = myJvmArgs
}

tasks.withType<JavaExec> {
    jvmArgs(myJvmArgs)
}