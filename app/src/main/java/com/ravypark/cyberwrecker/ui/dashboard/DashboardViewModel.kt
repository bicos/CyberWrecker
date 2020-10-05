package com.ravypark.cyberwrecker.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ravypark.cyberwrecker.data.Feed
import com.ravypark.cyberwrecker.utils.Event

class DashboardViewModel : ViewModel() {

    val clickEvent = MutableLiveData<Event<Feed>>()

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    private val collection = FirebaseFirestore.getInstance().collection("docs")

    fun getQuery(orderBy: String) : Query {
       return collection.orderBy(orderBy, Query.Direction.DESCENDING)
    }

    fun clickEvent(feed: Feed) {
        clickEvent.postValue(Event(feed))
    }
}