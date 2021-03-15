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
### Setup needed for calling plain old HTTP (This is not needed in case of HTTPS) 
1. Create 'xml' folder under resources
2. Create file `network_security_config.xml` with following config
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true"/>
</network-security-config>
```
3. Add following config to AndroidManifest to application declaration
```xml
        android:usesCleartextTraffic="true"
        android:networkSecurityConfig="@xml/network_security_config.xml"
```

### Add Retrofit & Gson dependency
- go to `app/build.gradle` and add to dependency section following lines
```groovy
    //helps with LiveData & coroutines dependecies
    implementation "androidx.fragment:fragment-ktx:1.3.1"

    //Retrofit setup
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
```
- do not forget to `Sync Project` after adding new dependencies

### Setup networking part
0. Add INTERNET permission to AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

1. create class `Grade` in the package `com.concur.dofeworkshop.model`
```kotlin
package com.concur.dofeworkshop.model

data class Grade(
    val subject: String = "",
    val grade: String = ""
)

```
2. create interface `GradeSystem` in the package `com.concur.dofeworkshop.network`
```kotlin
package com.concur.dofeworkshop.network

import com.concur.dofeworkshop.model.Grade
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GradeSystem {

    @GET("grades")
    fun getGrades(): Call<List<Grade>>

    @POST("grades")
    fun postGrade(@Body grade: Grade): Call<Unit>
}
```
3. Create NetworkClient class in the package `com.concur.dofeworkshop.network`. This class holds the whole networking setup
```kotlin
package com.concur.dofeworkshop.network

import android.util.Log
import com.concur.dofeworkshop.model.Grade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


class NetworkClient {
    private val TAG = "NetworkClient"
    private val API_URL = "https://private-9937d4-dofeandroidworkshop.apiary-mock.com/"

    private fun prepareService(): GradeSystem {
        //TODO: add code here
    }

    suspend fun getGrades(): List<Grade> {
        //TODO: add code here
        return  emptyList<Grade>()
    }

    suspend fun postGrade(subject: String, grade: String) : Boolean {
        //TODO: add code here
        return false
    }
}
```
4. Now lets add code that creates the service. Here you can choose between logless version and fully logging version:
```kotlin
    //logless version
    private fun prepareService(): GradeSystem {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(GradeSystem::class.java)
    }
```
```kotlin
    //fully logging version
    private fun prepareService(): GradeSystem {
        val loginInterceptor = HttpLoggingInterceptor()
            .apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

        val client = OkHttpClient
            .Builder()
            .addInterceptor(loginInterceptor)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(GradeSystem::class.java)
    }
```
5. The next step is to add code that handles downloading
```kotlin
    suspend fun getGrades(): List<Grade> {
        var result = emptyList<Grade>()
        try {
            val response = prepareService().getGrades().awaitResponse()
            if (response.isSuccessful) {
                result = response.body()!!
            } else {
                Log.w(TAG, "Something went wrong. Response code: ${response.code()}")
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Something failed.", ex)
        }
        return result
    }
```
5b. To test that everything is running fine you can add following lines to onCreate method in the MainActivity
```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "${NetworkClient().getGrades()}")
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
```
6. Let's add functionality for upload
```kotlin
    suspend fun postGrade(subject: String, grade: String): Boolean {
        return try {
            val response = prepareService().postGrade(Grade(subject, grade)).awaitResponse()
            response.isSuccessful
        } catch (ex: Exception) {
            Log.e(TAG, "Something failed.", ex)
            false
        }
    }
```
7. Final version of NetworkClient should look like this:
```kotlin
package com.concur.dofeworkshop.network

import android.util.Log
import com.concur.dofeworkshop.model.Grade
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


class NetworkClient {
    private val TAG = "NetworkClient"
    private val API_URL = "https://private-9937d4-dofeandroidworkshop.apiary-mock.com/"

    private fun prepareService(): GradeSystem {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(GradeSystem::class.java)
    }

    suspend fun getGrades(): List<Grade> {
        var result = emptyList<Grade>()
        try {
            val response = prepareService().getGrades().awaitResponse()
            if (response.isSuccessful) {
                result = response.body()!!
            } else {
                Log.w(TAG, "Something went wrong. Response code: ${response.code()}")
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Something failed.", ex)
        }
        return result
    }

    suspend fun postGrade(subject: String, grade: String): Boolean {
        return try {
            val response = prepareService().postGrade(Grade(subject, grade)).awaitResponse()
            response.isSuccessful
        } catch (ex: Exception) {
            Log.e(TAG, "Something failed.", ex)
            false
        }
    }
}
```


### Create MVVM model
1. Create class `GradeViewModel` in the package `com.concur.dofeworkshop`.
```kotlin
package com.concur.dofeworkshop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.concur.dofeworkshop.model.Grade
import com.concur.dofeworkshop.network.NetworkClient
import kotlinx.coroutines.launch

class GradeViewModel(private val networkClient: NetworkClient = NetworkClient()) : ViewModel() {

    val gradeList: MutableLiveData<List<Grade>> by lazy {
        MutableLiveData<List<Grade>>()
    }

    fun getAll() {
        viewModelScope.launch {
            gradeList.value = networkClient.getGrades()
        }
    }

    fun postNew(subject: String, grade: String) {
        viewModelScope.launch {
            if (networkClient.postGrade(subject, grade)) {
                getAll()
            }
        }
    }
}
``` 
2. The use of viewModel also requires hooking it to Activity. Let's modify the MainActivity to look like this:
```kotlin
package com.concur.dofeworkshop

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.concur.dofeworkshop.model.Grade

class MainActivity : AppCompatActivity() {      
    //TODO: add viewModel definition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: prepare gradelistObserver
        
        //TODO: hook gradeListObserver to viewModel
        
        //TODO: use viewModel to getAll grades from server
    
        //TODO: use viewModel to upload sample
    }
}
``` 
3. Adding viewModel definition
```kotlin
private val model: GradeViewModel by viewModels()
```
4. Preparation of gradeListObserver
```kotlin
val gradeListObserver = Observer<List<Grade>>{
            //adapter.update(it)
            Log.d(TAG, "Some update happened.")
}
```
5. Hook gradeListObserver to viewModel
```kotlin
model.gradeList.observe(this, gradeListObserver)
```
6. Let's use the view model to getAll grades and upload new item
```kotlin
model.getAll()
model.postNew("Mathematics", "B")
```
7. The final version of the MainActivity should look like this:
```kotlin
package com.concur.dofeworkshop

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.concur.dofeworkshop.model.Grade

class MainActivity : AppCompatActivity() {
    private val model: GradeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gradeListObserver = Observer<List<Grade>>{
            //adapter.update(it)
            Log.d(TAG, "Some update happened.")
        }
        model.gradeList.observe(this, gradeListObserver)

        model.getAll()
        model.postNew("Mathematics", "B")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
```