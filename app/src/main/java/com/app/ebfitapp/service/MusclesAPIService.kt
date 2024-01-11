package com.app.ebfitapp.service

import com.app.ebfitapp.BuildConfig
import com.app.ebfitapp.model.BodyPartExercises
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MusclesAPIService {

    private val BASE_URL = BuildConfig.RapidAPI_BASE

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MusclesAPI::class.java)

    fun getExercises(bodyPart: String) : Single<BodyPartExercises> {
        return api.getBodyPartExercises(bodyPart)
    }

}