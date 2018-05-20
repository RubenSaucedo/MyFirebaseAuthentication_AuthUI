# MyFirebaseAuthentication_AuthUI

Firebase give us really useful tools, one of them is the Authentication of users in Android Applications.
Also, Google is helping the developers to create an application really fast, taking Firebase Authentication
and a group of Front-End developers with years of experience.
They make AuthUI a really amazing Front-end UI to create a Sign in activity.

However, some times takes a while to read and catch all the new implementations that we have to do and use this tools.
The complete project of FirebaseUI for Auth is on:
* https://github.com/firebase/FirebaseUI-Android
 
This project simplify you and implement an easy way to get a Sign in activity using Firebase and AuthUI.
Just use it as the beginning of your project and customize it if you want.

## Getting Started

As a pre-requisite, make sure that your application is configured for use [Firebase](https://firebase.google.com/docs/?hl=es-419).

* Create a new project in the Firebase console
* Set the project name and the country
* Set the package name
* Set the nickname app (optional)
* Set the SHA-1 key

You can get your debug key in windows with the following command in the CMD:
Note: Change to your java directory.
For example:
```
cd C:\Program Files\Java\jdk1.8.0_131\bin
```
Type:
```
keytool -list -v -keystore C:\Users\%USER%\.android\debug.keystore -alias androiddebugkey -storepass android -keypass android
```
* Download the JSON file from firebase: google-services.json and copy into your app\ directory in your app

### Installing

1. Add the dependencies in the Project Gradle:
```
buildscript {
    
    repositories {
        mavenCentral()
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.2'
        classpath 'com.google.gms:google-services:3.2.0'

    }
}
```
2. Add the dependencies in the Module App Gradle:
```
    implementation 'com.google.firebase:firebase-core:12.0.1'
    implementation 'com.google.firebase:firebase-auth:12.0.1'
    implementation 'com.google.android.gms:play-services-auth:12.0.1'

    implementation 'com.firebaseui:firebase-ui-auth:3.3.1'
    implementation "com.android.support:design:27.1.1"

    implementation 'com.jakewharton:butterknife:8.8.1'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'

    implementation 'com.github.bumptech.glide:glide:4.7.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.7.1'
```
3. Create the class MyAppGlideModule. [Glide](https://bumptech.github.io/glide/)

```
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

}
```

### Customize the App

You can customize to your own style and requirements

```
/**
     * Set more Providers of Auth
     * new AuthUI.IdpConfig.FacebookBuilder().build(),
     * new AuthUI.IdpConfig.TwitterBuilder().build(),
     * Change Logo
     * setLogo(R.drawable.ownLogo)
     * SetTheme
     * setTheme(R.style.onwTheme)
     */
     
public void signIn(View view){
        startActivityForResult(
                //Get an instance of AuthUI based on the default app
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                ))
                .setTheme(R.style.PurpleTheme)
                .setLogo(R.drawable.ensa_logo)
                .setTosUrl(TOS_URL)
                .setPrivacyPolicyUrl(PRIVACY_POLICY)
                .build(),
                RC_SIGN_IN
        );
    }
```

## Versioning

We use [GitHub](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/resid9406/MyFirebaseAuthentication_AuthUI/tags). 

## Authors

* **Enrique Saucedo** - *Initial work* - [WebSite](www.enriquersaucedo.com)

## License

Copyright [2018] [Enrique Saucedo]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Acknowledgments

* Google team
* IPN

