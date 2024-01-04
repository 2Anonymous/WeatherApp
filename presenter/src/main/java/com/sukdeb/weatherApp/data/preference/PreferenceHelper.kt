package com.sukdeb.weatherApp.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceHelper {
    fun defaultPrefs(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    /**Edit and apply to shared preference*/
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     * Add entry in shared preference
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     * Get entry from shared preference
     */
    inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**Remove/Delete/Clear entry from shared preference*/
    fun SharedPreferences.remove(name: String) = edit().remove(name).apply()

    /**Clear shared preference*/
    fun SharedPreferences.clearPreference() = edit().clear().apply()

    /**Check if key exists (will return true/false)*/
    fun SharedPreferences.isContains(name: String): Boolean = contains(name)

    /*PrivatePreferenceStart*/
    const val SEARCH_FLAG = "searchFlag"
    const val NO_SEARCH_LIST_FLAG = "noSearchListFlag"
    const val FAMILY_PROFILE_FLAG = "familyProfileFlag"
    const val ADD_POST_FLAG = "addPostFlag"
    const val FAMILY_SHARE_FLAG = "familyShareFlag"
    const val KID_SHARE_FLAG = "kidShareFlag"
    const val INVITE_FLAG = "inviteFlag"
    const val NOTIFICATION_FLAG = "notificationFlag"
    const val FAVORITE_FLAG = "favoriteFlag"
    const val PRIVACY_FLAG = "privacyFlag"
    const val DEVICE_TOKEN = "firebase_token"
    /*PrivatePreferenceEnd*/

}