# --- Hilt / Dagger ---
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.HiltAndroidApp

# --- Retrofit / Moshi models & annotations ---
-keepattributes *Annotation*
-keep class com.example.interviewapp.data.model.** { *; }

# --- OkHttp/Retrofit warnings ---
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn javax.annotation.**
