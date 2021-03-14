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