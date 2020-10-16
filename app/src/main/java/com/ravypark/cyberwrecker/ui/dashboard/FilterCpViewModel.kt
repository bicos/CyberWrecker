package com.ravypark.cyberwrecker.ui.dashboard

import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.Gson
import com.ravypark.cyberwrecker.data.ContentProvider

class FilterCpViewModel : ViewModel() {

    private val config = Firebase.remoteConfig

    fun getConfigs(): List<ContentProvider> {
        val json = config.getString("config")
        return Gson().fromJson(json, Array<ContentProvider>::class.java).toList()
    }
}