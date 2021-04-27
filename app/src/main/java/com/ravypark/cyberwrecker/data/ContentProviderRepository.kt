package com.ravypark.cyberwrecker.data

interface ContentProviderRepository {

    fun getRemoteCps(): List<ContentProvider>

    fun getFilterCps(): MutableSet<String>

    fun removeCp(cp: String)

    fun addCp(cp: String)
}

