package com.ravypark.cyberwrecker.ui.dashboard

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.ravypark.cyberwrecker.R
import com.ravypark.cyberwrecker.data.Feed
import com.ravypark.cyberwrecker.utils.EventObserver
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*


class DashboardFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by viewModels { ViewModelProvider.NewInstanceFactory() }

    private lateinit var adapter: FirestorePagingAdapter<Feed, FeedListViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(5)
            .setPageSize(30)
            .build()

        val options = FirestorePagingOptions.Builder<Feed>()
            .setLifecycleOwner(this)
            .setQuery(dashboardViewModel.getQuery("createdAt"), config, Feed::class.java)
            .build()

        adapter = FeedListAdapter(options, dashboardViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        root.feed_list.setHasFixedSize(true)

        val deco = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL).apply {
            ContextCompat.getDrawable(requireContext(), R.drawable.divider_feed_list)?.let {
                setDrawable(it)
            }
        }
        root.feed_list.addItemDecoration(deco)

        root.refresh.setOnRefreshListener {
            adapter.refresh()
        }

        root.filter.setOnClickListener {
            FilterCpFragment().show(childFragmentManager, "filter_cp")
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dashboardViewModel.clickEvent.observe(viewLifecycleOwner, EventObserver {
            CustomTabsIntent.Builder()
                .build().launchUrl(requireContext(), Uri.parse(it.url))
        })

        dashboardViewModel.loadingState.observe(viewLifecycleOwner, {
            if (it == LoadingState.LOADED || it == LoadingState.ERROR) {
                refresh.isRefreshing = false
                if (it == LoadingState.ERROR) {
                    Toast.makeText(requireContext(), "일시적으로 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        dashboardViewModel.deleteSuccessEvent.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), "문서 삭제를 성공하였습니다.", Toast.LENGTH_SHORT).show()
        })

        dashboardViewModel.deleteFailureEvent.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), "문서 삭제를 실패하였습니다. (${it})", Toast.LENGTH_SHORT).show()
        })

        dashboardViewModel.start {
            feed_list.adapter = this.adapter
        }
    }
}