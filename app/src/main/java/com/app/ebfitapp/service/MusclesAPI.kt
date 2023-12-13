package com.app.ebfitapp.service

import com.app.ebfitapp.model.BodyPartExercises
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MusclesAPI {


    @Headers(
        "X-RapidAPI-Key: c7de494d29msh11c396e17354e22p1d1ee5jsn71bfc00fa0e2",
        "X-RapidAPI-Host: exercisedb.p.rapidapi.com"
    )
    @GET("exercises/bodyPart/{bodyPart}")
    fun getBodyPartExercises(@Path("bodyPart") bodyPart: String) : Single<BodyPartExercises>


}