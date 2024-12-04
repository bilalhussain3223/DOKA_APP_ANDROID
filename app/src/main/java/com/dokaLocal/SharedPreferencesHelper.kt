package com.dokaLocal

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject // Import the Inject annotation

class SharedPreferencesHelper @Inject constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("DokaPreferences", Context.MODE_PRIVATE)

    // Save the ID and Token
    fun saveIdAndToken(id: String, token: String) {
        sharedPreferences.edit().apply {
            putString("id", id)
            putString("token", token)
            apply()
        }
    }

    // Save workshop mode status
    fun setWorkshopMode(isEnabled: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean("workshopMode", isEnabled)
            apply()
        }
    }

    // Get the ID and Token
    fun getId(): String? {
        return sharedPreferences.getString("id", null)
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun isWorkshopModeEnabled(): Boolean = sharedPreferences.getBoolean("workshopMode", false)

    // Clear stored data
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
