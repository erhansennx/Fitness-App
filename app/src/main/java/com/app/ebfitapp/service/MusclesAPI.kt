package com.app.ebfitapp.service

import com.app.ebfitapp.model.BodyPartExercises
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MusclesAPI {


    @Headers(
        "X-RapidAPI-Key: b44c125e18msh4a2c8197e633d75p1d3c57jsnd70bb430c218",
        "X-RapidAPI-Host: exercisedb.p.rapidapi.com"
    )
    @GET("exercises/bodyPart/{bodyPart}")
    suspend fun getBodyPartExercises(@Path("bodyPart") bodyPart: String) : Single<BodyPartExercises>


}