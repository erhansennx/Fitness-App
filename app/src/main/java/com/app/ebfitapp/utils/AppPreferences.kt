package com.app.ebfitapp.utils

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    // Örneğin bir anahtar (key) ile bir değeri kaydetmek için
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // Örneğin bir anahtar (key) ile kaydedilmiş bir değeri almak için
    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    // Diğer türler için benzer yardımcı metodları ekleyebilirsiniz
    // Örneğin:
    // fun saveInt(key: String, value: Int) { ... }
    // fun getInt(key: String, defaultValue: Int): Int { ... }
    // vb.

    // Örneğin bir anahtar (key) ile bir değeri silmek için
    fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    // Tüm verileri temizlemek için
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}