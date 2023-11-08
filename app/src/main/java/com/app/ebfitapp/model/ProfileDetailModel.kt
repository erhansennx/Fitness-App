package com.app.ebfitapp.model

data class ProfileDetailModel(
    val username: String?,
    val email: String?,
    val gender: String?,
    val profileImageURL: String?,
    val age: String?,
    val height: Int?,
    val weight: Double?,
    val targetWeight: Double?,
    val goal: String?
)