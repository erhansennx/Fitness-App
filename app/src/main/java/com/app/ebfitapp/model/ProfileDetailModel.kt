package com.app.ebfitapp.model

data class ProfileDetailModel(
    var username: String?,
    var email: String?,
    var gender: String?,
    var profileImageURL: String?,
    var age: String?,
    var height: Int?,
    var weight: Double?,
    var targetWeight: Double?,
    var goal: String?
)