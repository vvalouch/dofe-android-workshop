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