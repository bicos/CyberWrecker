package com.ravypark.cyberwrecker.data

import android.content.SharedPreferences
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson

class ContentProviderRepositoryImpl(
    private val pref: SharedPreferences,
    private val config: FirebaseRemoteConfig
) : ContentProviderRepository {

    override fun getRemoteCps(): List<ContentProvider> {
        val json = config.getString("config")
        return Gson().fromJson(json, Array<ContentProvider>::class.java).toList()
    }

    override fun getFilterCps(): MutableSet<String> {
        return pref.getFilterCps()
    }

    override fun removeCp(cp: String) {
        pref.removeFilterCp(cp)
    }

    override fun addCp(cp: String) {
        pref.addFilterCp(cp)
    }
}