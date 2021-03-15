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