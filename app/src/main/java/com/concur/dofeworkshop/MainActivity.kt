package com.concur.dofeworkshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.concur.dofeworkshop.network.NetworkClient
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread(start = true){
            val networkClient = NetworkClient()
            print(networkClient.getGrades())

            print(networkClient.postGrade("Mathematics", "A"))
        }
    }
}