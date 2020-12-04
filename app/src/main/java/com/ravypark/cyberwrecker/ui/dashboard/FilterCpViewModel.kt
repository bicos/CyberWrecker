package com.ravypark.cyberwrecker.ui.dashboard

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.Gson
import com.ravypark.cyberwrecker.data.*
import com.ravypark.cyberwrecker.utils.Event

class FilterCpViewModel(app: Application) : AndroidViewModel(app) {

    private val config = Firebase.remoteConfig

    private val pref = (getApplication() as Context).getAppPreferences()

    val filterCpChangedEvent = MutableLiveData<Event<Set<String>>>()

    private val prefChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_FILTER_CP) {
                filterCpChangedEvent.value = Event(pref.getFilterCps())
            }
        }

    init {
        pref.registerOnSharedPreferenceChangeListener(prefChangedListener)
        filterCpChangedEvent.value = Event(pref.getFilterCps())
    }

    fun clickItem(cp: ContentProvider) {
        val filterCps = pref.getFilterCps()
        if (filterCps.contains(cp.cp)) {
            pref.removeFilterCp(cp.cp)
        } else {
            pref.addFilterCp(cp.cp)
        }
    }

    fun mapFilterCps(filterCps: Set<String>): List<FilterCpItemViewModel> {
        val configs = getConfigs()
        return configs.map {
            FilterCpItemViewModel(it, filterCps.contains(it.cp))
        }
    }

    private fun getConfigs(): List<ContentProvider> {
        val json = config.getString("config")
        return Gson().fromJson(json, Array<ContentProvider>::class.java).toList()
    }

    override fun onCleared() {
        super.onCleared()
        pref.unregisterOnSharedPreferenceChangeListener(prefChangedListener)
    }
}