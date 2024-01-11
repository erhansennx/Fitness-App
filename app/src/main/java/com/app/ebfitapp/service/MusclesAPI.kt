package com.app.ebfitapp.service

import com.app.ebfitapp.BuildConfig
import com.app.ebfitapp.model.BodyPartExercises
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MusclesAPI {

    @Headers(
        BuildConfig.RapidAPI_KEY,
        BuildConfig.RapidAPI_HOST
    )
    @GET("exercises/bodyPart/{bodyPart}")
    fun getBodyPartExercises(@Path("bodyPart") bodyPart: String) : Single<BodyPartExercises>

}