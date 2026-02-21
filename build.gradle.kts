plugins {
    id("java")
}

group = "mx.jume.andiemgcheff"
version = "1.0.9"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(fileTree("libs") { include("*.jar") })
    compileOnly("com.google.code.gson:gson:2.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// AndiemgCheff Manifest Generation
val generateManifest by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/resources")
    outputs.dir(outputDir)
    
    inputs.property("group", project.group)
    inputs.property("name", rootProject.name)
    inputs.property("version", project.version)

    doLast {
        val json = """
            {
              "Group": "${project.group}",
              "Name": "${rootProject.name}",
              "Version": "${project.version}",
              "ServerVersion": "2026.02.19-1a311a592",
              "Main": "${project.group}.AndiemgCheff",
              "Authors": [ 
                  { "Name": "jume" },
                  { "Name": "andiemg" },
                  { "Name": "antigravity" }
              ],
              "IncludesAssetPack": true
            }
        """.trimIndent()
        
        outputDir.get().file("manifest.json").asFile.apply {
            parentFile.mkdirs()
            writeText(json)
        }
    }
}

sourceSets {
    main {
        resources.srcDir(generateManifest)
    }
}

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to rootProject.name,
            "Implementation-Version" to project.version,
            "Main-Class" to "${project.group}.AndiemgCheff"
        )
    }
}
