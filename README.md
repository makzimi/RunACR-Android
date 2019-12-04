# RunACR-Android - Sound recognition SDK for Android

RunACR SDK allows your Android app to recognize sounds and display interactive content synchronized with what your users hear at that particular moment (like Shazam).

As of September 1st, 2018 SDK supports live streams. You can use SDK for recognizing TV and radio stations. You can also purchase SDK source codes for iOS, Android, and Web Server for unlimited use in your projects. Contact us at support@runacr.com to learn details.

![](http://runacr.com/images/2screen.png)

## Installation

### For Gradle users
Add this dependency in your app module:
```Gradle
dependencies {
    compile 'com.runacr.android.runacrsdk:android-runacr-sdk:1.0.4'
}
```

### For Non Gradle users
If you are not using Gradle Build Tool, just download (http://runacr.com/download) and include aar library to your project.

## Prepare
RunACR SDK works with preprocessed database files. Go to http://runacr.com/ and upload any audio files you want to be your database. Create a fingerprint database and download it - it will be a file with ".runacr" extension.

This sample app already includes test ".runacr" database. It is located in the assets folder. It contains fingerprints of 2 videos located in the "videos" folder.

## Initialize
Initialize library in your app by calling init function. Pass Api key, fingerprint file path and initialization callback:

```Java
RunACR.init(this, "Your api key", "Path to .runacr file",
       new InitListener() {

           @Override
           public void onSuccess() {
               //Ready to recognize
           }

           ...
       });
```
This sample app already includes test API Key which works with test ".runacr" file. So you don't need to do anything else to run this sample app.

## Run
Run audio recognition by calling recognize method and passing the callback RecognizeListener into it. You result will be in RecognizeResult object.
```Java
RunACR.recognize(new RecognizeListener() {

    @Override
    public void onSuccess(RecognizeResult recognizeResult) {
        // Recognized track id - recognizeResult.id
        // Absolute offset - recognizeResult.absoluteTimeOffset
        // Relative offset - recognizeResult.relativeTimeOffset
    }

    @Override
    public void onFailure ( final int errorCode ) {
        // Not found. Try again.
    }

    ...
});
```

## Contact
- Max Kachinkin
- support@runacr.com
- http://runacr.com
- iOS version: https://github.com/andrei200287/RunACR-iOS
