# --- Core Kotlin metadata ---
-keep class kotlin.Metadata { *; }
-keep class kotlin.coroutines.** { *; }
-dontwarn kotlin.**

# --- AndroidX Compose ---
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# --- Retrofit + Kotlin Serialization ---
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**
-dontwarn kotlinx.serialization.**

# --- Coil ---
-dontwarn coil3.**
-keep class coil3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# --- Hilt / Dagger ---
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class hilt_aggregated_deps.** { *; }
-dontwarn dagger.**
-dontwarn javax.inject.**

# --- Room ---
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# --- AndroidX Lifecycle/ViewModel ---
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

# --- Keep your app's code ---
-keep class com.moseti.cutecats.** { *; }

# --- Strip Log calls ---
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# --- Optional: Strip debug info ---
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable
