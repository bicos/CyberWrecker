package com.ravypark.cyberwrecker.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.ravypark.cyberwrecker.R
import com.ravypark.cyberwrecker.data.Feed
import com.ravypark.cyberwrecker.utils.Event

class DashboardViewModel @ViewModelInject constructor(
    private val config: FirebaseRemoteConfig
) : ViewModel() {

    val clickEvent = MutableLiveData<Event<Feed>>()

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    val collection = Firebase.firestore.collection("docs_v2")

    fun start(callback: () -> Unit) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        config.setConfigSettingsAsync(configSettings).addOnSuccessListener {
            config.setDefaultsAsync(R.xml.default_config).addOnSuccessListener {
                config.fetchAndActivate().addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback.invoke()
                    } else {
                        loadingState.value = LoadingState.ERROR
                    }
                }
            }.addOnFailureListener {
                loadingState.value = LoadingState.ERROR
            }
        }.addOnFailureListener {
            loadingState.value = LoadingState.ERROR
        }
    }

    fun clickEvent(feed: Feed) {
        clickEvent.postValue(Event(feed))
    }
}