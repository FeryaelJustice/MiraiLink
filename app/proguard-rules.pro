# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class kotlin.Metadata { *; }
-keepattributes *Annotation*

# Kotlinx Serialization (suele evitar problemas raros en release)
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

-keepclassmembers class **$Companion {
    public kotlinx.serialization.KSerializer serializer(...);
}

-keepclassmembers class ** {
    public static kotlinx.serialization.KSerializer **$serializer;
}

-keep class com.feryaeljustice.mirailink.kotzilla.** { *; }
-keep class io.kotzilla.** { *; }

# Ktor engines (si usas el engine okhttp o el "native" de Kotzilla)
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**