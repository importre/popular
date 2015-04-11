# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/importre/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-keep class io.github.importre.popular.api.** { *; }
-dontwarn io.github.importre.popular.MainFragment$**

-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-keep class rx.** { *; }
-keep class retrofit.** { *; }
-keep class okio.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn kotlin.**,kotlinx.**,retrofit.**,rx.**,okio.**
-dontwarn com.squareup.okhttp.**

#-keepclasseswithmembers class * {
#    @retrofit.http.* <methods>;
#}

-keepattributes Exceptions,InnerClasses,Signature,Deprecated
-keepattributes SourceFile,LineNumberTable,EnclosingMethod
