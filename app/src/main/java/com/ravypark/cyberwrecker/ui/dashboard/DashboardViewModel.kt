package com.ravypark.cyberwrecker.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.FirebaseFirestore
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

    val deleteSuccessEvent = MutableLiveData<Event<Void>>()

    val deleteFailureEvent = MutableLiveData<Event<Exception>>()

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    private val collection = Firebase.firestore.collection("docs")

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

    fun deleteClick(feed: Feed) {
        collection.document(feed.getItemId()).delete().addOnSuccessListener {
            deleteSuccessEvent.postValue(Event(it))
        }.addOnFailureListener {
            deleteFailureEvent.postValue(Event(it))
        }
    }
}