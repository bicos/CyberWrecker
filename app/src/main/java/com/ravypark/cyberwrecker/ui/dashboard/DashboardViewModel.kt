package com.ravypark.cyberwrecker.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ravypark.cyberwrecker.data.Feed

class DashboardViewModel : ViewModel() {

    val clickEvent = MutableLiveData<Feed>()

    private val collection = FirebaseFirestore.getInstance().collection("docs")

    fun getQuery(orderBy: String) : Query {
       return collection.orderBy(orderBy, Query.Direction.DESCENDING)
    }

    fun clickEvent(feed: Feed) {
        clickEvent.postValue(feed)
    }
}