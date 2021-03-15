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