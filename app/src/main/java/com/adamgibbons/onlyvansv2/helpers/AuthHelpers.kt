package com.adamgibbons.onlyvansv2.helpers

import android.content.Context
import android.content.SharedPreferences
import com.adamgibbons.onlyvansv2.R

class AuthHelpers(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(R.string.app_name.toString(), Context.MODE_PRIVATE)

    var userLoggedIn: Boolean
        get() = preferences.getBoolean(R.string.user_logged_in.toString(), false)
        set(value) = preferences.edit().putBoolean(R.string.user_logged_in.toString(), value).apply()
}