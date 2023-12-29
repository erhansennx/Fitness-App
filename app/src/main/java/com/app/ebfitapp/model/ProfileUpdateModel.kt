package com.app.ebfitapp.model

data class ProfileUpdateModel(
    var username: String?,
    val profileImageURL: String?,
    var age: String?,
    var weight: Double?,
    var targetWeight: Double?
 // diğerleri null dönmeyecek mi n nasıl update yapiyz bazi belli verilerde digerleri etkilenmez
    )