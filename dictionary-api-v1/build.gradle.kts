plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.openapi)
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir("/generate-resources/main/src/main/kotlin"))
    }
}

openApiGenerate {
    val openapiGroup = "com.tonyp.dictionary.api.v1"
    generatorName.set("kotlin")
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set(
        layout.projectDirectory
            .file("/src/main/resources/specs-meaning-v1.yaml")
            .asFile.absolutePath
    )

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    configOptions.set(
        mapOf(
            "library" to "multiplatform",
            "enumPropertyNaming" to "UPPERCASE",
            "collectionType" to "list"
        )
    )
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}