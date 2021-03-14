package com.concur.dofeworkshop.network

import android.util.Log
import com.concur.dofeworkshop.model.GradeDTO
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class NetworkClient {
    private val TAG = "NetworkClient"
    private val API_URL = "https://private-9937d4-dofeandroidworkshop.apiary-mock.com/"

    private fun getClient(): GradeSystem {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient
                .Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
                .build())
            .build()
        return retrofit.create(GradeSystem::class.java)
    }


    fun getGrades(): List<GradeDTO> {
        try {
            val response = getClient().getGrades().execute()
            if (response.isSuccessful) {
                return response.body()!!
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Something failed.", ex)
        }
        return emptyList()
    }

    fun postGrade(subject: String, grade: String) {
        try {
            getClient().postGrade(GradeDTO(subject, grade)).execute()
        } catch (ex: Exception) {
            Log.e(TAG, "Something failed.", ex)
        }
    }
}