# Rules for Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Rules for Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.mgaetan89.showsrage.model.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Rules for OkHttp
-dontwarn com.squareup.okhttp.**

# Rules for Okio
-dontwarn okio.**

# Rules for Play Services
-dontwarn com.google.android.gms.common.GooglePlayServicesUtil

# Rules for Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# Rules for Serializable
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Rules for the Support Library
-keep class android.support.v7.widget.SearchView { *; }
