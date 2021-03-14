# DofE Android Mini-Workshop

## Goal
The goal is to build a simple Android App which uses the API build during the session

## The API
The app can be tested against mock server: 
```https://private-9937d4-dofeandroidworkshop.apiary-mock.com/```

### GET grades
Sample response:
```json
[
  { "subject" : "Mathematics", "grade" : "A" }
]
```

### POST
Sample body of the post
```json
{ "subject" : "Mathematics", "grade" : "A" }
```


## Tasks
1. Add Retrofit & Gson dependency
- go to `app/build.gradle` and add to dependency section following lines
```groovy
    //Retrofit setup
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```
- do not forget to `Sync Project` after adding new dependencies

2. Create Model & Interface for calling API
- create class `Grade` in the package `com.concur.dofeworkshop.model`
3. Create MVVM model 