package com.ravypark.cyberwrecker.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.ravypark.cyberwrecker.R
import com.ravypark.cyberwrecker.data.Feed
import com.ravypark.cyberwrecker.utils.Event

class DashboardViewModel : ViewModel() {

    val clickEvent = MutableLiveData<Event<Feed>>()

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    private val collection = Firebase.firestore.collection("docs_v2")

    private val config = Firebase.remoteConfig.apply {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        this.setConfigSettingsAsync(configSettings)
    }

    init {
        config.setDefaultsAsync(R.xml.default_config)
    }

    fun start(callback: () -> Unit) {
        config.fetchAndActivate().addOnCompleteListener {
            callback.invoke()
        }
    }

    fun getQuery(orderBy: String): Query {
        return collection.orderBy(orderBy, Query.Direction.DESCENDING)
    }

    fun clickEvent(feed: Feed) {
        clickEvent.postValue(Event(feed))
    }
}