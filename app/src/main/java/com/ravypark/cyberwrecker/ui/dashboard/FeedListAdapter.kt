package com.ravypark.cyberwrecker.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.ravypark.cyberwrecker.data.Feed
import com.ravypark.cyberwrecker.databinding.ViewholderFeedBinding
import com.ravypark.cyberwrecker.utils.dp
import java.util.concurrent.atomic.AtomicBoolean

class FeedListAdapter(
    options: FirestorePagingOptions<Feed>,
    private val viewModel: DashboardViewModel
) : FirestorePagingAdapter<Feed, FeedListViewHolder>(options) {

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewholderFeedBinding.inflate(inflater, parent, false)
        return FeedListViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: FeedListViewHolder, position: Int, model: Feed) {
        viewHolder.bind(model, viewModel)
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        loadingState.postValue(state)
    }
}

class FeedListViewHolder(private val binding: ViewholderFeedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(feed: Feed, viewModel: DashboardViewModel) {
        binding.feed = feed
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(url: String?) {
    if (url == null) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        Glide.with(this).load(url)
            .transform(RoundedCorners(3.dp))
            .into(this)
            .clearOnDetach()
    }
}

@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.let {
        setOnClickListener(OnSingleClickListener(it))
    }
}

class OnSingleClickListener(
    private val clickListener: View.OnClickListener,
    private val intervalMs: Long = 1000
) : View.OnClickListener {
    private var canClick = AtomicBoolean(true)

    override fun onClick(v: View?) {
        if (canClick.getAndSet(false)) {
            v?.run {
                postDelayed({
                    canClick.set(true)
                }, intervalMs)
                clickListener.onClick(v)
            }
        }
    }
}