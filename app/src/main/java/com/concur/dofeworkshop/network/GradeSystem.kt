package com.concur.dofeworkshop.network

import com.concur.dofeworkshop.model.GradeDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GradeSystem {

    @GET("grades")
    fun getGrades(): Call<List<GradeDTO>>

    @POST("grades")
    fun postGrade(@Body gradeDTO: GradeDTO): Call<Unit>

}