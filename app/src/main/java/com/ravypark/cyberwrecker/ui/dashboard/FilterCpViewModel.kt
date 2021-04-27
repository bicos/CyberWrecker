package com.ravypark.cyberwrecker.ui.dashboard

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ravypark.cyberwrecker.data.*
import com.ravypark.cyberwrecker.utils.Event

class FilterCpViewModel @ViewModelInject constructor(
    private val pref: SharedPreferences,
    private val repo: ContentProviderRepository
) : ViewModel() {

    val filterCpChangedEvent = MutableLiveData<Event<Set<String>>>()

    private val prefChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_FILTER_CP) {
                filterCpChangedEvent.value = Event(repo.getFilterCps())
            }
        }

    init {
        pref.registerOnSharedPreferenceChangeListener(prefChangedListener)
        filterCpChangedEvent.value = Event(repo.getFilterCps())
    }

    fun clickItem(cp: ContentProvider) {
        val filterCps = repo.getFilterCps()
        if (filterCps.contains(cp.cp)) {
            repo.removeCp(cp.cp)
        } else {
            repo.addCp(cp.cp)
        }
    }

    fun mapFilterCps(filterCps: Set<String>): List<FilterCpItemViewModel> {
        return repo.getRemoteCps().map {
            FilterCpItemViewModel(it, filterCps.contains(it.cp))
        }
    }

    override fun onCleared() {
        super.onCleared()
        pref.unregisterOnSharedPreferenceChangeListener(prefChangedListener)
    }
}