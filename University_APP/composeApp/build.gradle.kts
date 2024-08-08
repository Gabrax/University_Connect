import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform) // Kotlin Multiplatform plugin alias
    alias(libs.plugins.jetbrainsCompose)    // JetBrains Compose plugin alias
    kotlin("plugin.serialization") version "2.0.0"
    alias(libs.plugins.compose.compiler) // This might be redundant if covered by JetBrains Compose
}




kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("com.squareup.moshi:moshi:1.15.1")
            implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
            implementation("io.github.jan-tennert.supabase:serializer-moshi:VERSION")
            implementation(project.dependencies.platform("io.github.jan-tennert.supabase:bom:2.6.0-beta-1"))
            implementation("io.github.jan-tennert.supabase:postgrest-kt")
            implementation("io.github.jan-tennert.supabase:gotrue-kt")
            implementation("io.github.jan-tennert.supabase:realtime-kt")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
            implementation("io.ktor:ktor-client-cio:2.3.4")
            implementation("io.ktor:ktor-client-core:2.3.4")
            implementation("io.ktor:ktor-client-json:2.3.4")
            implementation("io.ktor:ktor-client-serialization:2.3.4")
            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
            implementation ("androidx.compose.runtime:runtime-livedata:$1.6.8")
            implementation("ch.qos.logback:logback-classic:1.2.3")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "uni_connect.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Uni_Connect"
            packageVersion = "1.0.0"
            jvmArgs(
                "--add-modules=java.sql",
                "-Dorg.slf4j.simpleLogger.defaultLogLevel=debug"
            )

        }
    }
}
