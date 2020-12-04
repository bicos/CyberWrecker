package com.ravypark.cyberwrecker.data

import android.content.Context
import android.content.SharedPreferences

const val DEFAULT_PREF_KEY = "default"
const val KEY_FILTER_CP = "filter_cp"

fun Context.getAppPreferences(): SharedPreferences =
    getSharedPreferences(DEFAULT_PREF_KEY, Context.MODE_PRIVATE)

fun SharedPreferences.getFilterCps(): MutableSet<String> =
    getStringSet(KEY_FILTER_CP, emptySet()) ?: mutableSetOf()

fun SharedPreferences.setFiltersCps(filterCps: Set<String>) =
    edit().putStringSet(KEY_FILTER_CP, filterCps).apply()

fun SharedPreferences.addFilterCp(filterCp: String) {
    val filterCps = getFilterCps().toMutableSet()
    filterCps.add(filterCp)
    setFiltersCps(filterCps)
}

fun SharedPreferences.removeFilterCp(filterCp: String) {
    val filterCps = getFilterCps().toMutableSet()
    filterCps.remove(filterCp)
    setFiltersCps(filterCps)
}