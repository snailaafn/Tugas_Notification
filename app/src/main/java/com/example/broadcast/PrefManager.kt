package com.example.broadcast

import android.content.Context

class PrefManager private constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        PREFS_FILENAME, Context.MODE_PRIVATE
    )

    companion object{
        private const val PREFS_FILENAME = "AuthAppPref"
        private const val KEY_USERNAME = "username"

        @Volatile
        private var instance:PrefManager? = null
        fun getInstance(context: Context): PrefManager{
            return instance?: synchronized(this){
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    //shared pref itu butuh editor untuk ganti isiny
    fun saveUsername(username: String){
        val editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun getUsername(): String{
        return sharedPreferences.getString(KEY_USERNAME, "")?: "" // pk tnd tanya bisa null
    }

    fun clear(){
        sharedPreferences.edit().also {  //ini gampangnya dari sek bagian editor kl mls bikin variabel
            it.clear()
            it.apply()
        }
    }
}