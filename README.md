android-sample-twitter4j-auth
=============================

## About this sample
This is sample project using Twitter4J Authentication/Authorization on Android.

## Problem on Android with Networking operation
Networking operation on Main thread(e.g. ```onCreate```), Android throws "android.os.NetworkOnMainThreadException." 
Writing code below to ignoring Exception, but should limit it to be used on a test.

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

## Move networking operation to other tasks
This sample is using [``Loader``](http://developer.android.com/reference/android/support/v4/content/Loader.html) and [``LoaderManager.LoaderCallbacks``](http://developer.android.com/reference/android/support/v4/app/LoaderManager.LoaderCallbacks.html) in the Android Support Library.
